package org.example.corebridgebatch;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class CorebridgeBatchApplication {

    private static final String PUSHGATEWAY_URL =
            "175.197.41.64:33388";

    // 모듈 분리 전/후 구분을 위한 job 이름
    private static final String JOB_NAME = "corebridge_batch_job"; // 모듈 분리 후
    // private static final String JOB_NAME = "corebridge_batch_job_legacy"; // 비교용 레거시

    public static void main(String[] args) {

        log.info("========================================");
        log.info("🚀 CoreBridge Batch Application 시작");
        log.info("📦 Architecture: Separated Module");
        log.info("========================================");

        try {
            ConfigurableApplicationContext context =
                    new SpringApplicationBuilder(CorebridgeBatchApplication.class)
                            .web(WebApplicationType.NONE) // 웹 서버 비활성화
                            .run(args);

            int exitCode = SpringApplication.exit(context);

            log.info("========================================");
            log.info("🔚 CoreBridge Batch Application 종료 (Exit Code: {})", exitCode);
            log.info("========================================");

            System.exit(exitCode);

        } catch (Exception e) {
            log.error("❌ 배치 애플리케이션 실행 중 오류 발생", e);
            System.exit(1);
        }
    }

    @Bean
    public CommandLineRunner runBatch(JobLauncher jobLauncher,
                                      Job notificationMaintenanceJob) {
        return args -> {

            log.info("🎯 [Batch] Job 실행 준비");
            log.info("📊 [Metric] Job Name: {}", JOB_NAME);

            long startTime = System.currentTimeMillis();
            Instant startInstant = Instant.now();

            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", startInstant.toEpochMilli())
                    .addString("executionId", UUID.randomUUID().toString())
                    .toJobParameters();

            JobExecution execution;

            try {
                execution = jobLauncher.run(notificationMaintenanceJob, params);
            } catch (Exception e) {
                log.error("❌ Batch Job 실행 오류", e);
                long duration = System.currentTimeMillis() - startTime;
                pushFailMetric(duration, e.getMessage());
                throw new RuntimeException(e);
            }

            // Duration 계산
            long duration = System.currentTimeMillis() - startTime;

            log.info("==================================================");
            log.info("✔ Status: {}", execution.getStatus());
            log.info("✔ ExitStatus: {}", execution.getExitStatus());
            log.info("✔ Duration: {} ms ({} sec)", duration, duration / 1000.0);
            log.info("==================================================");

            // Step별 상세 정보 로깅
            execution.getStepExecutions().forEach(se -> {
                log.info("Step [{}] → Read: {}, Write: {}, Skip: {}, Commit: {}",
                        se.getStepName(),
                        se.getReadCount(),
                        se.getWriteCount(),
                        se.getSkipCount(),
                        se.getCommitCount()
                );
            });

            if (execution.getStatus() == BatchStatus.COMPLETED) {
                pushSuccessMetric(duration, execution);
            } else {
                pushFailMetric(duration, execution.getExitStatus().getExitDescription());
            }
        };
    }

    // ======================================================
    // 🔥 PushGateway: 성공 Metric (확장)
    // ======================================================
    private void pushSuccessMetric(long durationMs, JobExecution execution) {
        try {
            CollectorRegistry registry = new CollectorRegistry();

            // 1. 실행 시간
            Gauge duration = Gauge.build()
                    .name("corebridge_batch_last_duration_ms")
                    .help("Last CoreBridge batch duration (ms)")
                    .register(registry);

            // 2. 마지막 실행 시간
            Gauge lastRun = Gauge.build()
                    .name("corebridge_batch_last_run_timestamp")
                    .help("Last batch run timestamp")
                    .register(registry);

            // 3. 성공/실패 플래그
            Gauge successFlag = Gauge.build()
                    .name("corebridge_batch_success_flag")
                    .help("1=success, 0=fail")
                    .register(registry);

            // 4. 에러 플래그 (성공 시 0)
            Gauge errorFlag = Gauge.build()
                    .name("corebridge_batch_error_flag")
                    .help("1 if last batch failed")
                    .register(registry);

            // 5. 처리된 레코드 수
            Gauge processedRecords = Gauge.build()
                    .name("corebridge_batch_processed_records")
                    .help("Total records processed in last batch")
                    .register(registry);

            // 6. 실행 횟수 (누적)
            Counter executionCount = Counter.build()
                    .name("corebridge_batch_execution_total")
                    .help("Total batch execution count")
                    .register(registry);

            duration.set(durationMs);
            lastRun.set(Instant.now().getEpochSecond());
            successFlag.set(1);
            errorFlag.set(0);

            // Step별 처리 레코드 합산
            long totalProcessed = execution.getStepExecutions().stream()
                    .mapToLong(se -> se.getWriteCount())
                    .sum();
            processedRecords.set(totalProcessed);

            executionCount.inc();

            PushGateway pg = new PushGateway(PUSHGATEWAY_URL);
            pg.pushAdd(registry, JOB_NAME);

            log.info("📡 [PushGateway] 성공 메트릭 push 완료");
            log.info("   ├─ Duration: {} ms", durationMs);
            log.info("   ├─ Processed Records: {}", totalProcessed);
            log.info("   └─ Job: {}", JOB_NAME);

        } catch (Exception e) {
            log.error("❌ PushGateway 성공 metric push 실패", e);
        }
    }

    // ======================================================
    // 🔥 PushGateway: 실패 Metric (확장)
    // ======================================================
    private void pushFailMetric(long durationMs, String message) {
        try {
            CollectorRegistry registry = new CollectorRegistry();

            Gauge duration = Gauge.build()
                    .name("corebridge_batch_last_duration_ms")
                    .help("Last batch duration (ms)")
                    .register(registry);

            Gauge lastRun = Gauge.build()
                    .name("corebridge_batch_last_run_timestamp")
                    .help("Last batch run timestamp")
                    .register(registry);

            Gauge successFlag = Gauge.build()
                    .name("corebridge_batch_success_flag")
                    .help("1=success, 0=fail")
                    .register(registry);

            Gauge errorFlag = Gauge.build()
                    .name("corebridge_batch_error_flag")
                    .help("1 if last batch failed")
                    .register(registry);

            Counter errorCount = Counter.build()
                    .name("corebridge_batch_error_total")
                    .help("Total batch error count")
                    .register(registry);

            duration.set(durationMs);
            lastRun.set(Instant.now().getEpochSecond());
            successFlag.set(0);
            errorFlag.set(1);
            errorCount.inc();

            PushGateway pg = new PushGateway(PUSHGATEWAY_URL);
            pg.pushAdd(registry, JOB_NAME);

            log.error("📡 [PushGateway] 실패 메트릭 push 완료");
            log.error("   ├─ Duration: {} ms", durationMs);
            log.error("   ├─ Error: {}", message);
            log.error("   └─ Job: {}", JOB_NAME);

        } catch (Exception e) {
            log.error("❌ PushGateway 실패 metric push 실패", e);
        }
    }
}