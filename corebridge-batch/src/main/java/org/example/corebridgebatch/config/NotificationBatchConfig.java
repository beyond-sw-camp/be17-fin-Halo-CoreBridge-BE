package org.example.corebridgebatch.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.corebridgebatch.notification.model.entity.Notification;
import org.example.corebridgebatch.notification.model.enums.DeliveryStatus;
import org.example.corebridgebatch.notification.repository.NotificationRepository;
import org.example.corebridgebatch.notification.service.NotificationService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class NotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final NotificationRepository repository;
    private final NotificationService service;

    private static final int CHUNK_SIZE = 100;
    private static final long OLD_THRESHOLD_DAYS = 30;
    private static final int MAX_RETRY_COUNT = 5;

    @PostConstruct
    public void init() {
        log.info("🔧 NotificationBatchConfig 초기화 완료");
    }

    // ========================================================================
    // 1) 재전송 Step
    // ========================================================================
    @Bean
    public Step resendStep() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("resend-task-");
        executor.setConcurrencyLimit(4);

        return new StepBuilder("resendStep", jobRepository)
                .<Notification, Notification>chunk(CHUNK_SIZE, txManager)
                .reader(resendReader())
                .processor(resendProcessor())
                .writer(resendWriter())
                .taskExecutor(executor)
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

    @Bean
    public ItemWriter<Notification> resendWriter() {
        return items -> {
            repository.saveAll(items);
            log.info("💾 재전송 {}건 완료", items.size());
        };
    }

    // ========================================================================
    // 2) 오래된 알림 정리 Step
    // ========================================================================
    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    long threshold = System.currentTimeMillis()
                            - (OLD_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L);

                    repository.deleteOldSentNotifications(threshold);
                    log.info("🧹 오래된 알림 정리 완료");
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // ========================================================================
    // 3) 재시도 초과 삭제 Step
    // ========================================================================
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
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // ========================================================================
    // 4) JOB 구성 + After Metrics Listener
    // ========================================================================
    @Bean
    public Job notificationMaintenanceJob() {
        return new JobBuilder("notificationMaintenanceJob", jobRepository)
                .start(resendStep())
                .next(cleanupStep())
                .next(failedCleanupStep())
                .listener(pushAfterMetricsListener()) // metrics push
                .build();
    }

    // ========================================================================
    // 5) AFTER Metrics Push (corebridge-batch)
    // ========================================================================
    @Bean
    public JobExecutionListener pushAfterMetricsListener() {

        return new JobExecutionListener() {

            Gauge durationGauge = Gauge.build()
                    .name("corebridge_batch_last_duration_ms")
                    .help("Batch Duration (After Separation)")
                    .labelNames("module")
                    .register();

            Gauge processedGauge = Gauge.build()
                    .name("corebridge_batch_processed_count")
                    .help("Processed Count (After Separation)")
                    .labelNames("module")
                    .register();

            Gauge failedGauge = Gauge.build()
                    .name("corebridge_batch_failed_count")
                    .help("Failed Count (After Separation)")
                    .labelNames("module")
                    .register();

            Gauge jobStatusGauge = Gauge.build()
                    .name("corebridge_batch_status")
                    .help("Batch Job Status (0=FAILED, 1=COMPLETED)")
                    .labelNames("module")
                    .register();

            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("🎬 배치 작업 시작");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

                // 💥 TRY/CATCH 밖에서 실행 → 실패해도 반드시 실행됨
                log.info("📌 [AfterJob Listener] 실행됨. (성공/실패 무조건)");

                try {
                    PushGateway pg = new PushGateway("pushgateway-prometheus-pushgateway.monitor.svc.cluster.local:9091");

                    long start = jobExecution.getStartTime().atZone(java.time.ZoneId.systemDefault())
                            .toInstant().toEpochMilli();
                    long end = jobExecution.getEndTime().atZone(java.time.ZoneId.systemDefault())
                            .toInstant().toEpochMilli();
                    long duration = end - start;

                    long processed = jobExecution.getStepExecutions().stream()
                            .mapToLong(StepExecution::getWriteCount).sum();

                    long failed = jobExecution.getStepExecutions().stream()
                            .mapToLong(StepExecution::getSkipCount).sum();

                    boolean isSuccess = jobExecution.getStatus() == BatchStatus.COMPLETED;

                    durationGauge.labels("corebridge-batch").set(duration);
                    processedGauge.labels("corebridge-batch").set(processed);
                    failedGauge.labels("corebridge-batch").set(failed);

                    // 🔥 성공 = 1, 실패 = 0 으로 기록
                    jobStatusGauge.labels("corebridge-batch").set(isSuccess ? 1 : 0);

                    log.info("📊 AFTER metrics: duration={}ms, processed={}, failed={}, success={}",
                            duration, processed, failed, isSuccess);

                    // 🔥 Job 성공/실패와 상관없이 pushAdd 실행
                    pg.pushAdd(CollectorRegistry.defaultRegistry, "corebridge_after_job");

                } catch (Exception e) {
                    log.error("❌ After metrics push 실패", e);
                }

            }
        };
    }


}
