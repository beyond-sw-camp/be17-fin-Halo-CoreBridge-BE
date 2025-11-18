package com.halo.core_bridge.config;

import com.halo.core_bridge.api.schedule.notification.model.entity.Notification;
import com.halo.core_bridge.api.schedule.notification.model.enums.DeliveryStatus;
import com.halo.core_bridge.api.schedule.notification.repository.NotificationRepository;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class BatchConfig {

    private final JobLauncher jobLauncher;
    private final Job notificationMaintenanceJob;
    private final NotificationRepository repository;
    private final NotificationService service;

    private static final int CHUNK_SIZE = 100;
    private static final long OLD_THRESHOLD_DAYS = 30;
    private static final int MAX_RETRY_COUNT = 5;

    // 1) 알림 재전송 Step
    @Bean
    public Step resendStep(JobRepository jobRepository,
                           PlatformTransactionManager txManager) {

        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("resend-task-");
        executor.setConcurrencyLimit(4);

        return new StepBuilder("resendStep", jobRepository)
                .<Notification, Notification>chunk(CHUNK_SIZE, txManager)
                .reader(resendReader())
                .processor(resendProcessor())
                .writer(resendWriter())
                .taskExecutor(executor)
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
            service.tryDeliver(item);
            return item;
        };
    }

    @Bean
    public ItemWriter<Notification> resendWriter() {
        return items -> {
            repository.saveAll(items);
            log.info("🔁 재전송 완료 {}건", items.size());
        };
    }

    // 2) 오래된 알림 정리 Step
    @Bean
    public Step cleanupStep(JobRepository jobRepository,
                            PlatformTransactionManager txManager) {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    long threshold = System.currentTimeMillis()
                            - OLD_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L;

                    repository.deleteOldSentNotifications(threshold);
                    log.info("🧹 오래된 알림 정리 완료");

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // 3) 재시도 초과 실패 알림 삭제 Step
    @Bean
    public Step failedCleanupStep(JobRepository jobRepository,
                                  PlatformTransactionManager txManager) {
        return new StepBuilder("failedCleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<Notification> failed = repository.findFailedNotifications(MAX_RETRY_COUNT);
                    if (!failed.isEmpty()) {
                        repository.deleteAll(failed);
                        log.warn("❌ 재시도 초과 알림 {}건 삭제", failed.size());
                    }
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // 4) Job 구성
    @Bean
    public Job notificationMaintenanceJob(JobRepository jobRepository,
                                          Step resendStep,
                                          Step cleanupStep,
                                          Step failedCleanupStep) {

        return new JobBuilder("notificationMaintenanceJob", jobRepository)
                .start(resendStep)
                .next(cleanupStep)
                .next(failedCleanupStep)
                .build();
    }

    // 5) 스케줄러
    @Scheduled(fixedDelay = 300_000)
    public void runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", Instant.now().toEpochMilli())
                .toJobParameters();

        log.info("🚀 배치 실행 시작");
        jobLauncher.run(notificationMaintenanceJob, params); // ⬅ 수정된 정답
    }
}
