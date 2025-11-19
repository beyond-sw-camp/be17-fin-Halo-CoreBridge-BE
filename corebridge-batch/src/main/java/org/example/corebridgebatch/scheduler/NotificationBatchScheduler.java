package org.example.corebridgebatch.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job notificationMaintenanceJob;

    /**
     * 5분마다 알림 유지보수 배치 작업 실행
     */
    @Scheduled(fixedDelay = 300_000)
    public void runNotificationMaintenanceJob() {
        try {
            log.info("🚀 [Spring Batch] Notification Maintenance Job 실행 시작");

            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", Instant.now().toEpochMilli())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(notificationMaintenanceJob, params);

            log.info("✅ [Spring Batch] Job 실행 완료 - Status: {}, ExitStatus: {}",
                    execution.getStatus(), execution.getExitStatus());

        } catch (Exception e) {
            log.error("❌ [Spring Batch] Job 실행 실패", e);
        }
    }
}
