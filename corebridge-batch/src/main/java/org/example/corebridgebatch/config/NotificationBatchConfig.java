package org.example.corebridgebatch.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.corebridgebatch.notification.model.entity.Notification;
import org.example.corebridgebatch.notification.model.enums.DeliveryStatus;
import org.example.corebridgebatch.notification.repository.NotificationRepository;
import org.example.corebridgebatch.notification.service.NotificationService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class NotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final JobLauncher jobLauncher;

    private final NotificationRepository repository;
    private final NotificationService service;

    private static final int CHUNK_SIZE = 100;
    private static final long OLD_THRESHOLD_DAYS = 30;
    private static final int MAX_RETRY_COUNT = 5;

    @Bean
    public RepositoryItemReader<Notification> resendReader() {
        return new RepositoryItemReaderBuilder<Notification>()
                .repository(repository)
                .methodName("findForRetry")
                .arguments(List.of(List.of(DeliveryStatus.UNSENT)))
                .pageSize(CHUNK_SIZE)
                .sorts(Map.of("timestamp", Sort.Direction.ASC))
                .name("resendReader")
                .build();
    }

    /**
     * ✅ Processor에서는 절대 DB 저장을 하지 않는다.
     *    - service.tryDeliver(item)는 "알림 전송 + 엔티티 필드 변경"까지만 수행
     *    - repository.save(...) 는 여기서 호출 금지
     */
    @Bean
    public ItemProcessor<Notification, Notification> resendProcessor() {
        return item -> {
            try {
                service.tryDeliver(item);
                return item;
            } catch (Exception e) {
                log.error("❌ 재전송 실패 - id={}, err={}", item.getId(), e.getMessage());
                return item;
            }
        };
    }

    /**
     * ✅ Writer 에서만 실제 DB UPDATE 수행
     *    - chunk 단위로 한 번에 saveAll
     *    - 멀티 스레드 없이 순차 실행이라 락 충돌이 훨씬 줄어듦
     */
    @Bean
    public ItemWriter<Notification> resendWriter() {
        return items -> {
            if (items.isEmpty()) return;

            repository.saveAll(items);
            log.info("💾 재전송 {}건 저장 완료", items.size());
        };
    }

    /**
     * ✅ resendStep: 단일 스레드 chunk 기반
     *    - taskExecutor 제거 → RepositoryItemReader + Paging과 안전하게 사용
     */
    @Bean
    public Step resendStep() {
        return new StepBuilder("resendStep", jobRepository)
                .<Notification, Notification>chunk(CHUNK_SIZE, txManager)
                .reader(resendReader())
                .processor(resendProcessor())
                .writer(resendWriter())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        log.info("📥 resendStep 시작");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("📤 resendStep 완료 - Read={}, Write={}, Skip={}",
                                stepExecution.getReadCount(),
                                stepExecution.getWriteCount(),
                                stepExecution.getSkipCount());
                        return ExitStatus.COMPLETED;
                    }
                })
                .build();
    }

    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    long threshold = System.currentTimeMillis()
                            - (OLD_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L);

                    int deleted = repository.deleteOldSentNotifications(threshold);
                    log.info("🧹 오래된 알림 정리 완료 - deleted={}, 기준일 {}일", deleted, OLD_THRESHOLD_DAYS);
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    @Bean
    public Step failedCleanupStep() {
        return new StepBuilder("failedCleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<Notification> failed = repository.findFailedNotifications(MAX_RETRY_COUNT);

                    if (!failed.isEmpty()) {
                        failed.forEach(n ->
                                log.warn("⚠️ 재시도 초과 삭제 → id={}, retry={}", n.getId(), n.getRetryCount())
                        );
                        repository.deleteAll(failed);
                        log.info("🧾 재시도 초과 알림 삭제 완료 - count={}", failed.size());
                    } else {
                        log.info("🧾 재시도 초과 알림 없음");
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    /**
     * ✅ 통합 Job + After Metrics Listener
     */
    @Bean
    public Job notificationMaintenanceJob() {
        return new JobBuilder("notificationMaintenanceJob", jobRepository)
                .start(resendStep())
                .next(cleanupStep())
                .next(failedCleanupStep())
                .listener(pushAfterMetricsListener())
                .build();
    }

    /**
     * ✅ After Metrics: 성공/실패 상관없이 항상 실행
     *    - CorebridgeBatchApplication.java의 메트릭과 함께 사용
     *    - 이 Listener는 추가적인 모듈별 메트릭 제공
     */
    @Bean
    public JobExecutionListener pushAfterMetricsListener() {

        // ------ Bean 메서드 스코프에서 한 번만 등록 ------
        final Gauge startTimeGauge = Gauge.build()
                .name("corebridge_batch_start_timestamp")
                .help("After Batch Start Time (epoch millis)")
                .register();

        final Gauge durationGauge = Gauge.build()
                .name("corebridge_batch_last_duration_ms")
                .help("After Batch Duration (ms)")
                .register();

        final Gauge processedGauge = Gauge.build()
                .name("corebridge_batch_processed_count")
                .help("After Batch Processed Count")
                .register();

        final Gauge failedGauge = Gauge.build()
                .name("corebridge_batch_failed_count")
                .help("After Batch Failed Count")
                .register();

        final Gauge statusGauge = Gauge.build()
                .name("corebridge_batch_status")
                .help("After Batch Status (1=success, 0=fail)")
                .register();

        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                long start = jobExecution.getStartTime()
                        .atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli();

                startTimeGauge.set(start);
                log.info("🎬 After Batch Job 시작 (timestamp={})", start);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

                log.info("📌 [AfterJob Listener] 실행됨");

                try {
                    PushGateway pg = new PushGateway("175.197.41.64:33388");

                    long start = jobExecution.getStartTime()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli();

                    long end = jobExecution.getEndTime()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli();

                    long duration = end - start;

                    long processed = jobExecution.getStepExecutions().stream()
                            .mapToLong(StepExecution::getWriteCount)
                            .sum();

                    long failed = jobExecution.getStepExecutions().stream()
                            .mapToLong(StepExecution::getSkipCount)
                            .sum();

                    boolean success = jobExecution.getStatus() == BatchStatus.COMPLETED;

                    // ---- 메트릭 값 주입 ----
                    durationGauge.set(duration);
                    processedGauge.set(processed);
                    failedGauge.set(failed);
                    statusGauge.set(success ? 1 : 0);

                    log.info("📊 AFTER metrics: duration={} ms, processed={}, failed={}, success={}",
                            duration, processed, failed, success);

                    // ---- PushGateway 전송 ----
                    pg.pushAdd(CollectorRegistry.defaultRegistry, "corebridge_batch_job");

                    log.info("✅ PushGateway After 메트릭 Push 완료");

                } catch (Exception e) {
                    log.error("❌ After metrics push 실패", e);
                }
            }
        };
    }

    /**
     * ⚠️ @Scheduled 메서드 삭제됨
     *
     * 이유:
     * - CorebridgeBatchApplication.java의 CommandLineRunner가 이미 배치 실행 담당
     * - CronJob 환경에서는 @Scheduled가 작동하지 않음 (Pod가 즉시 종료되므로)
     * - 중복 실행 방지
     *
     * 배치 실행 흐름:
     * 1. CronJob이 5분마다 새 Pod 생성
     * 2. CorebridgeBatchApplication.main() 실행
     * 3. CommandLineRunner runBatch() 자동 실행
     * 4. notificationMaintenanceJob() 실행
     * 5. 모든 Step 완료
     * 6. pushAfterMetricsListener() 메트릭 전송
     * 7. CorebridgeBatchApplication의 메트릭 전송
     * 8. System.exit() → Pod 종료
     */
}