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
     * ✔ Before(corebridge-core)에서는 더 이상 Spring Batch는 실행되지 않음.
     * ✔ 오직 "분리 이전(Before)" 시뮬레이션 메트릭만 PushGateway로 전송.
     * ✔ After와 metric 이름이 절대 충돌하지 않도록 prefix 완전 분리.
     */

    private static final Gauge START_TIME_GAUGE = Gauge.build()
            .name("corebridge_core_start_timestamp")
            .help("Before (corebridge-core) - Start Timestamp")
            .register();

    private static final Gauge DURATION_GAUGE = Gauge.build()
            .name("corebridge_core_last_duration_ms")
            .help("Before (corebridge-core) - Duration ms")
            .register();

    private static final Gauge PROCESSED_GAUGE = Gauge.build()
            .name("corebridge_core_processed_count")
            .help("Before (corebridge-core) - Processed Count")
            .register();

    private static final Gauge FAILED_GAUGE = Gauge.build()
            .name("corebridge_core_failed_count")
            .help("Before (corebridge-core) - Failed Count")
            .register();

    private PushGateway pushGateway;

    @PostConstruct
    public void init() {
        log.info("📡 [Before Metrics] BatchConfig 초기화 (corebridge-core / 메트릭 전용)");
        this.pushGateway = new PushGateway("175.197.41.64:33388");
    }

    /**
     * ✔ 30초마다 Before 메트릭 푸시
     * ✔ 랜덤 값으로 분리 이전 상태를 시뮬레이션
     */
    @Scheduled(fixedDelay = 300_000)
    public void pushBeforeMetrics() {
        try {
            double duration = 500 + Math.random() * 200;   // 500~700ms
            double processed = 50 + Math.random() * 30;    // 50~80건
            double failed = Math.random() * 3;             // 0~3건

            START_TIME_GAUGE.set(System.currentTimeMillis());
            DURATION_GAUGE.set(duration);
            PROCESSED_GAUGE.set(processed);
            FAILED_GAUGE.set(failed);

            log.info("📊 [Before] duration={}ms, processed={}, failed={}",
                    duration, processed, failed);

            pushGateway.pushAdd(CollectorRegistry.defaultRegistry, "corebridge_before_job");

        } catch (Exception e) {
            log.error("❌ BEFORE metric push error", e);
        }
    }
}