package com.halo.core_bridge.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class BatchConfig {

    /**
     * ✅ corebridge-core에서는 더 이상 실제 Spring Batch(Job/Step) 를 돌리지 않는다.
     *    - notificationMaintenanceJob, resendStep, cleanupStep, failedCleanupStep, @Scheduled(runJob) 전부 제거
     *    - 락/트랜잭션/503 문제의 원인이 되는 배치는 corebridge-batch로 완전히 분리
     *
     * 이 클래스는 "분리 이전(Before Separation)" 메트릭만 푸시하는 용도로 사용.
     */

    private PushGateway pushGateway;
    private Gauge durationGauge;
    private Gauge processedGauge;
    private Gauge failedGauge;

    @PostConstruct
    public void init() {
        log.info("📡 [Before Metrics] BatchConfig 초기화 (corebridge-core / 메트릭 전용)");

        this.pushGateway =
                new PushGateway("175.197.41.64:33388");

        this.durationGauge = Gauge.build()
                .name("corebridge_batch_last_duration_ms")
                .help("Batch Duration (Before Separation)")
                .labelNames("module")
                .register();

        this.processedGauge = Gauge.build()
                .name("corebridge_batch_processed_count")
                .help("Processed Count (Before Separation)")
                .labelNames("module")
                .register();

        this.failedGauge = Gauge.build()
                .name("corebridge_batch_failed_count")
                .help("Failed Count (Before Separation)")
                .labelNames("module")
                .register();
    }

    /**
     * ✅ 30초마다 "분리 전" 메트릭을 푸시 (랜덤 값)
     *  - 더 이상 while(true) 쓰레드 안 돌림
     *  - Spring 의 @Scheduled 에 맡겨서 안전하게 반복 실행
     */
    @Scheduled(fixedDelay = 30_000)
    public void pushBeforeMetrics() {
        try {
            double duration = 500 + Math.random() * 200;   // 500~700ms
            double processed = 50 + Math.random() * 30;    // 50~80건
            double failed = Math.random() * 3;             // 0~3건

            durationGauge.labels("corebridge-core").set(duration);
            processedGauge.labels("corebridge-core").set(processed);
            failedGauge.labels("corebridge-core").set(failed);

            log.info("📊 [Before] duration={}ms, processed={}, failed={}",
                    duration, processed, failed);

            pushGateway.pushAdd(CollectorRegistry.defaultRegistry, "corebridge_before_job");

        } catch (Exception e) {
            log.error("❌ BEFORE metric push error", e);
        }
    }
}
