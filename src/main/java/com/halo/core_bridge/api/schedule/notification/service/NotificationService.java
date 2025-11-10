package com.halo.core_bridge.api.schedule.notification.service;

import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import com.halo.core_bridge.api.schedule.notification.model.entity.Notification;
import com.halo.core_bridge.api.schedule.notification.model.enums.DeliveryStatus;
import com.halo.core_bridge.api.schedule.notification.repository.NotificationRepository;
import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {

    // ========== 설정 ==========
    private static final long SSE_TIMEOUT_MS = 30 * 60 * 1000L; // 30분
    private static final String EVENT_NAME = "notification";
    private static final long HEARTBEAT_INTERVAL_MS = 10_000L; // 10초
    private static final int MAX_RETRY_COUNT = 5;
    private static final long OLD_NOTIFICATION_THRESHOLD_DAYS = 30;

    private final NotificationRepository repository;
    private final UserRepository userRepository;

    // userId별로 여러 연결을 지원
    private final Map<Long, Set<SseEmitter>> emittersByUser = new ConcurrentHashMap<>();

    // 디버그용: Heartbeat 전송 통계
    private int heartbeatSentCount = 0;
    private int heartbeatSuccessCount = 0;
    private int heartbeatFailCount = 0;

    // ========== SSE 구독 관리 ==========

    /**
     * SSE 구독 등록
     */
    public SseEmitter subscribe(Long userId, UserRoleType role, String lastEventId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        // 유저별 emitter 세트에 추가
        emittersByUser.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(emitter);

        // 연결 종료 시 자동 제거
        Runnable cleanup = () -> {
            Set<SseEmitter> emitters = emittersByUser.get(userId);
            if (emitters != null) {
                emitters.remove(emitter);
                if (emitters.isEmpty()) {
                    emittersByUser.remove(userId);
                }
            }
            log.info("SSE 연결 종료: userId={}, 남은 연결={}개",
                    userId, getConnectedUserCount());
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(() -> {
            log.warn("SSE 타임아웃: userId={}", userId);
            cleanup.run();
        });
        emitter.onError(e -> {
            log.error("SSE 에러 발생: userId={}, error={}", userId, e.getMessage());
            cleanup.run();
        });

        // 연결 확인 메시지 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("ok")
                    .comment("SSE connection established"));

            log.info("✅ SSE 연결 성공: userId={}, totalUsers={}, totalConnections={}",
                    userId, getConnectedUserCount(), getTotalConnectionCount());

            // 연결 직후 미전송 알림 재전송
            resendUnsentNotifications(userId);

        } catch (IOException e) {
            log.error("초기 연결 메시지 전송 실패: userId={}", userId, e);
            cleanup.run();
        }

        return emitter;
    }

    /**
     * 특정 유저의 미전송 알림을 재전송
     */
    private void resendUnsentNotifications(Long userId) {
        List<Notification> unsent = repository.findUnsentByUserId(userId);
        if (!unsent.isEmpty()) {
            log.info("미전송 알림 재전송 시작: userId={}, count={}", userId, unsent.size());
            unsent.forEach(this::tryDeliver);
        }
    }

    // ========== 알림 생성 및 전송 ==========

    @Transactional
    public NotificationDto.Response createAndDispatch(NotificationDto.Request req) {
        if (req.isRoleBased()) {
            List<Long> userIds = userRepository.findIdsByRole(req.getRole());
            if (userIds.isEmpty()) {
                log.warn("Role에 해당하는 유저 없음: role={}", req.getRole());
                Notification placeholder = repository.save(req.toEntity(null));
                return NotificationDto.Response.from(placeholder);
            }

            Notification first = null;
            for (Long uid : userIds) {
                Notification n = repository.save(req.toEntity(uid));
                tryDeliver(n);
                if (first == null) first = n;
            }

            log.info("Role 기반 알림 발송 완료: role={}, count={}", req.getRole(), userIds.size());
            return NotificationDto.Response.from(first);

        } else {
            Notification n = repository.save(req.toEntity(req.getUserId()));
            tryDeliver(n);
            log.info("개인 알림 발송: userId={}, title={}", req.getUserId(), req.getTitle());
            return NotificationDto.Response.from(n);
        }
    }

    @Transactional(noRollbackFor = Exception.class)
    public void tryDeliver(Notification n) {
        if (n.getUserId() == null) {
            log.warn("userId가 null인 알림: id={}", n.getId());
            return;
        }

        Set<SseEmitter> emitters = emittersByUser.get(n.getUserId());
        boolean anyDelivered = false;

        if (emitters != null && !emitters.isEmpty()) {
            for (SseEmitter emitter : new ArrayList<>(emitters)) {
                boolean delivered = safeSend(emitter, SseEmitter.event()
                        .name(EVENT_NAME)
                        .id(String.valueOf(n.getId()))
                        .data(NotificationDto.Response.from(n)));

                if (delivered) {
                    anyDelivered = true;
                }
            }
        }

        if (anyDelivered) {
            n.markSent();
            log.debug("알림 전송 성공: id={}, userId={}", n.getId(), n.getUserId());
        } else {
            n.increaseRetry();
            log.debug("알림 전송 실패: id={}, userId={}, retryCount={}",
                    n.getId(), n.getUserId(), n.getRetryCount());
        }

        repository.save(n);
    }

    private boolean safeSend(SseEmitter emitter, SseEmitter.SseEventBuilder event) {
        try {
            emitter.send(event);
            return true;
        } catch (IOException e) {
            log.warn("Emitter 전송 실패: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Emitter 전송 중 예외: {}", e.getMessage(), e);
            return false;
        }
    }

    // ========== 알림 조회 ==========

    @Transactional(readOnly = true)
    public List<NotificationDto.Response> getUserNotifications(Long userId) {
        List<Notification> notifications = repository.findByUserIdOrderByTimestampDesc(userId);
        return notifications.stream()
                .map(NotificationDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return repository.countUnreadByUserId(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        repository.findById(notificationId).ifPresent(n -> {
            if (n.getUserId().equals(userId)) {
                n.markSent();
                repository.save(n);
                log.debug("알림 읽음 처리: id={}, userId={}", notificationId, userId);
            }
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = repository.findByUserIdOrderByTimestampDesc(userId);
        notifications.forEach(n -> {
            if (n.getStatus() == DeliveryStatus.UNSENT) {
                n.markSent();
            }
        });
        repository.saveAll(notifications);
        log.info("모든 알림 읽음 처리: userId={}, count={}", userId, notifications.size());
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        repository.findById(notificationId).ifPresent(n -> {
            if (n.getUserId().equals(userId)) {
                repository.delete(n);
                log.debug("알림 삭제: id={}, userId={}", notificationId, userId);
            }
        });
    }

    // ========== 스케줄러 ==========

    /**
     * Heartbeat (연결 유지)
     * - 10초마다 실행
     * - 모든 연결에 heartbeat 전송
     */
    @Scheduled(fixedDelay = HEARTBEAT_INTERVAL_MS)
    public void sendHeartbeat() {
        if (emittersByUser.isEmpty()) {
            // 연결된 사용자가 없을 때는 로그 생략
            return;
        }

        heartbeatSentCount = 0;
        heartbeatSuccessCount = 0;
        heartbeatFailCount = 0;

        String timestamp = Instant.now().toString();

        for (Map.Entry<Long, Set<SseEmitter>> entry : emittersByUser.entrySet()) {
            Long userId = entry.getKey();
            Set<SseEmitter> emitters = entry.getValue();

            for (SseEmitter em : new ArrayList<>(emitters)) {
                heartbeatSentCount++;

                boolean sent = safeSend(em, SseEmitter.event()
                        .name("heartbeat")
                        .data(timestamp)
                        .comment("Keep-alive heartbeat"));

                if (sent) {
                    heartbeatSuccessCount++;
                } else {
                    heartbeatFailCount++;
                    log.warn("💔 Heartbeat 전송 실패: userId={}", userId);
                }
            }
        }

        log.info("💓 Heartbeat 전송 완료: users={}, total={}, success={}, fail={}",
                emittersByUser.size(), heartbeatSentCount, heartbeatSuccessCount, heartbeatFailCount);
    }

    /**
     * 오래된 알림 자동 삭제
     * - 매일 새벽 2시에 실행
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupOldNotifications() {
        long threshold = System.currentTimeMillis() -
                (OLD_NOTIFICATION_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L);

        try {
            repository.deleteOldSentNotifications(threshold);
            log.info("오래된 알림 삭제 완료: threshold={}일 전", OLD_NOTIFICATION_THRESHOLD_DAYS);
        } catch (Exception e) {
            log.error("오래된 알림 삭제 실패", e);
        }
    }

    /**
     * 전송 실패한 알림 처리
     * - 매시간 정각에 실행
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void handleFailedNotifications() {
        List<Notification> failed = repository.findFailedNotifications(MAX_RETRY_COUNT);

        if (failed.isEmpty()) {
            return;
        }

        for (Notification n : failed) {
            log.warn("알림 전송 최종 실패: id={}, userId={}, retryCount={}, title={}",
                    n.getId(), n.getUserId(), n.getRetryCount(), n.getTitle());
            repository.delete(n);
        }

        log.info("전송 실패 알림 처리 완료: count={}", failed.size());
    }

    // ========== 모니터링 ==========

    public int getConnectedUserCount() {
        return emittersByUser.size();
    }

    public int getUserConnectionCount(Long userId) {
        Set<SseEmitter> emitters = emittersByUser.get(userId);
        return emitters != null ? emitters.size() : 0;
    }

    public int getTotalConnectionCount() {
        return emittersByUser.values().stream()
                .mapToInt(Set::size)
                .sum();
    }

    public Map<String, Object> getConnectionStatus() {
        return Map.of(
                "connectedUsers", getConnectedUserCount(),
                "totalConnections", getTotalConnectionCount(),
                "heartbeatStats", Map.of(
                        "sent", heartbeatSentCount,
                        "success", heartbeatSuccessCount,
                        "fail", heartbeatFailCount
                ),
                "userConnections", emittersByUser.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().size()
                        ))
        );
    }
}
