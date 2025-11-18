package org.example.corebridgebatch.config;


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
import org.springframework.batch.item.*;
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
        log.info("🔧 [Batch Config] NotificationBatchConfig 초기화 완료");
        log.info("📊 [Batch Config] CHUNK_SIZE: {}, OLD_THRESHOLD_DAYS: {}, MAX_RETRY_COUNT: {}",
                CHUNK_SIZE, OLD_THRESHOLD_DAYS, MAX_RETRY_COUNT);
    }

    // ===================================================
    // 1️⃣ 알림 재전송 Step (비동기 병렬 처리)
    // ===================================================
    @Bean
    public Step resendStep() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("resend-task-");
        executor.setConcurrencyLimit(4); // 병렬 실행 제한

        return new StepBuilder("resendStep", jobRepository)
                .<Notification, Notification>chunk(CHUNK_SIZE, txManager)
                .reader(resendReader())
                .processor(resendProcessor())
                .writer(resendWriter())
                .taskExecutor(executor)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        log.info("📥 [Step-1] resendStep 시작");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("📤 [Step-1] resendStep 완료 - Read: {}, Write: {}, Skip: {}",
                                stepExecution.getReadCount(),
                                stepExecution.getWriteCount(),
                                stepExecution.getSkipCount());
                        return ExitStatus.COMPLETED;
                    }
                })
                .build();
    }

    /**
     * 알림 재전송 Reader
     * Pageable은 자동으로 주입되므로 arguments()에는 도메인 인자만 전달.
     */
    @Bean
    public RepositoryItemReader<Notification> resendReader() {
        return new RepositoryItemReaderBuilder<Notification>()
                .repository(repository)
                .methodName("findForRetry") // Repository 메서드 이름
                .arguments(List.of(List.of(DeliveryStatus.UNSENT))) // ✅ Pageable 제외
                .pageSize(CHUNK_SIZE)
                .sorts(Map.of("timestamp", Sort.Direction.ASC))
                .name("resendReader")
                .build();
    }

    /**
     * 알림 재전송 Processor
     * 실패 시 retryCount 증가 로직은 NotificationService 내부에 포함되어야 함.
     */
    @Bean
    public ItemProcessor<Notification, Notification> resendProcessor() {
        return item -> {
            try {
                log.debug("🔄 [Processor] 알림 재전송 시도 - id: {}, userId: {}, retryCount: {}",
                        item.getId(), item.getUserId(), item.getRetryCount());
                service.tryDeliver(item);
                return item;
            } catch (Exception e) {
                log.error("❌ [Processor] 알림 재전송 실패 - id: {}, error: {}",
                        item.getId(), e.getMessage());
                return item; // 실패해도 Writer로 전달하여 상태 저장
            }
        };
    }

    /**
     * 알림 재전송 Writer
     */
    @Bean
    public ItemWriter<Notification> resendWriter() {
        return items -> {
            repository.saveAll(items);
            log.info("💾 [Writer] 재전송 처리 완료 {}건", items.size());
        };
    }

    // ===================================================
    // 2️⃣ 오래된 알림 정리 Step (단일 스레드)
    // ===================================================
    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("🧹 [Step-2] cleanupStep 시작");
                    long threshold = System.currentTimeMillis()
                            - (OLD_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L);

                    try {
                        repository.deleteOldSentNotifications(threshold);
                        log.info("✅ [Step-2] 오래된 알림 정리 완료 (기준일 {}일)", OLD_THRESHOLD_DAYS);
                    } catch (Exception e) {
                        log.error("❌ [Step-2] 알림 정리 실패", e);
                        throw e;
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // ===================================================
    // 3️⃣ 재시도 초과 실패 처리 Step
    // ===================================================
    @Bean
    public Step failedCleanupStep() {
        return new StepBuilder("failedCleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("🗑️ [Step-3] failedCleanupStep 시작");

                    try {
                        List<Notification> failed = repository.findFailedNotifications(MAX_RETRY_COUNT);
                        if (!failed.isEmpty()) {
                            failed.forEach(n -> log.warn("⚠️ 재시도 초과 알림 삭제: id={}, userId={}, retryCount={}, title={}",
                                    n.getId(), n.getUserId(), n.getRetryCount(), n.getTitle()));
                            repository.deleteAll(failed);
                            log.info("✅ [Step-3] 재시도 초과 알림 삭제 완료 count={}", failed.size());
                        } else {
                            log.info("✅ [Step-3] 삭제 대상 없음");
                        }
                    } catch (Exception e) {
                        log.error("❌ [Step-3] 실패 알림 정리 중 오류", e);
                        throw e;
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // ===================================================
    // 4️⃣ 통합 Job 구성
    // ===================================================
    @Bean
    public Job notificationMaintenanceJob() {
        return new JobBuilder("notificationMaintenanceJob", jobRepository)
                .start(resendStep())
                .next(cleanupStep())
                .next(failedCleanupStep())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("🎬 [Job] notificationMaintenanceJob 시작 - JobId: {}",
                                jobExecution.getJobId());
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("🎬 [Job] notificationMaintenanceJob 완료 - Status: {}, ExitStatus: {}",
                                jobExecution.getStatus(), jobExecution.getExitStatus());
                    }
                })
                .build();
    }
}
