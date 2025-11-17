package org.example.batch.config;

import com.halo.core_bridge.api.schedule.notification.model.entity.Notification;
import com.halo.core_bridge.api.schedule.notification.model.enums.DeliveryStatus;
import com.halo.core_bridge.api.schedule.notification.repository.NotificationRepository;
import com.halo.core_bridge.batch.service.NotificationBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Spring Batch 설정
 * 
 * ✅ 별도 서버로 분리된 이유:
 * 1. K8s 환경에서 API Pod 여러 개 뜨면 배치가 중복 실행됨
 * 2. API 서버 성능 저하 방지
 * 3. 장애 격리 (배치 에러가 API에 영향 없음)
 * 
 * 실행 주기: 5분마다 (300초)
 */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Log4j2
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final JobLauncher jobLauncher;

    private final NotificationRepository repository;
    private final NotificationBatchService batchService;

    private static final int CHUNK_SIZE = 100;
    private static final long OLD_THRESHOLD_DAYS = 30;
    private static final int MAX_RETRY_COUNT = 5;

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
            batchService.tryDeliver(item);
            return item;
        };
    }

    @Bean
    public ItemWriter<Notification> resendWriter() {
        return items -> {
            repository.saveAll(items);
            log.info("📝 [Batch] 재전송 완료 {}건", items.size());
        };
    }

    // ===================================================
    // 2️⃣ 오래된 알림 정리 Step
    // ===================================================
    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    long threshold = System.currentTimeMillis()
                            - (OLD_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L);
                    repository.deleteOldSentNotifications(threshold);
                    log.info("🧹 [Batch] 오래된 알림 정리 완료 (기준일 {}일)", OLD_THRESHOLD_DAYS);
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
                    List<Notification> failed = repository.findFailedNotifications(MAX_RETRY_COUNT);
                    if (!failed.isEmpty()) {
                        failed.forEach(n -> log.warn("❌ 재시도 초과 알림 삭제: id={}, userId={}, retryCount={}, title={}",
                                n.getId(), n.getUserId(), n.getRetryCount(), n.getTitle()));
                        repository.deleteAll(failed);
                        log.info("🧾 [Batch] 재시도 초과 알림 삭제 완료 count={}", failed.size());
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
                .build();
    }

    // ===================================================
    // 5️⃣ 스케줄러 (5분마다 실행)
    // ===================================================
    @Scheduled(fixedDelay = 300_000)
    public void runJob() throws Exception {
        log.info("🚀 [Spring Batch] Notification Maintenance Job 실행 시작");

        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", Instant.now().toEpochMilli())
                .toJobParameters();

        jobLauncher.run(notificationMaintenanceJob(), params);
    }
}
