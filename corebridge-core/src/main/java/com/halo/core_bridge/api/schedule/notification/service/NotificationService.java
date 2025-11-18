package com.halo.core_bridge.api.schedule.notification.service;

import com.halo.core_bridge.api.schedule.notification.infra.RedisNotificationPublisher;
import com.halo.core_bridge.api.schedule.notification.infra.SseEmitterManager;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {

    // ========== 설정 ==========
    private static final long SSE_TIMEOUT_MS = 30 * 60 * 1000L; // 30분
    private static final String EVENT_NAME = "notification";
    private static final int MAX_RETRY_COUNT = 5;
    private static final long OLD_NOTIFICATION_THRESHOLD_DAYS = 30;

    private final NotificationRepository repository;
    private final UserRepository userRepository;

    // ✅ [REDIS 단계 추가]
    private final RedisNotificationPublisher redisPublisher;

    // ✅ [중앙 집중식 SSE 연결 관리]
    private final SseEmitterManager emitterManager;

    // ========== SSE 구독 관리 ==========

    /**
     * SSE 구독 엔드포인트 핸들러
     *
     * @param userId 구독할 사용자 ID
     * @param role 사용자 역할 (현재는 미사용, 향후 확장 가능)
     * @param lastEventId 마지막으로 받은 이벤트 ID (재연결 시 사용)
     * @return SseEmitter 인스턴스
     */
    public SseEmitter subscribe(Long userId, UserRoleType role, String lastEventId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        // 연결 종료 시 정리 작업
        Runnable cleanup = () -> {
            emitterManager.remove(userId, emitter);
            log.info("SSE 연결 종료: userId={}, 남은 연결={}개",
                    userId, emitterManager.getConnectedUserCount());
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

        // Emitter 등록
        emitterManager.register(userId, emitter);

        try {
            // 초기 연결 확인 메시지 전송
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("ok")
                    .comment("SSE connection established"));

            log.info("✅ SSE 연결 성공: userId={}, totalUsers={}, totalConnections={}",
                    userId, emitterManager.getConnectedUserCount(),
                    emitterManager.getTotalConnectionCount());

            // 재연결 시 미전송 알림 재전송
            resendUnsentNotifications(userId);

        } catch (IOException e) {
            log.error("초기 연결 메시지 전송 실패: userId={}", userId, e);
            cleanup.run();
        }

        return emitter;
    }

    /**
     * 재연결 시 미전송 알림 재전송
     * - 최근 24시간 이내의 UNSENT 알림만 조회하여 전송
     *
     * @param userId 재전송 대상 사용자 ID
     */
    private void resendUnsentNotifications(Long userId) {
        List<Notification> unsent = repository.findUnsentByUserId(userId);
        if (!unsent.isEmpty()) {
            log.info("미전송 알림 재전송 시작: userId={}, count={}", userId, unsent.size());
            unsent.forEach(this::tryDeliver);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationDto.Response> getEmailTargetNotifications() {

        // UNSENT + SENT but not READ + not EMAIL_SENT
        List<Notification> targets = repository.findEmailTargets();

        log.info("📧 [NotificationService] 이메일 대상 조회: {}건", targets.size());

        return targets.stream()
                .map(n -> {
                    // 1) user 이메일 조회
                    String email = userRepository.findEmailByUserId(n.getUserId());

                    // 2) email 포함한 Response 반환
                    return NotificationDto.Response.from(n, email);
                })
                .collect(Collectors.toList());
    }

    // ========== 알림 생성 및 전송 ==========

    /**
     * 알림 생성 및 발송
     * - Role 기반: 해당 역할을 가진 모든 사용자에게 발송
     * - User 기반: 특정 사용자에게만 발송
     *
     * @param req 알림 요청 정보
     * @return 생성된 알림 정보
     */
    @Transactional
    public NotificationDto.Response createAndDispatch(NotificationDto.Request req) {
        if (req.isRoleBased()) {
            // Role 기반 알림 발송
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
            // 개인 알림 발송
            Notification n = repository.save(req.toEntity(req.getUserId()));
            tryDeliver(n);
            log.info("개인 알림 발송: userId={}, title={}", req.getUserId(), req.getTitle());
            return NotificationDto.Response.from(n);
        }
    }

    /**
     * ================================
     * ✅ 기존 1단계 (단일 서버) 코드
     * ================================
     */
    /*
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
    */

    /**
     * ================================
     * ✅ [REDIS 단계] 알림 전송 (중복 방지)
     * ================================
     *
     * 알림 전송 프로세스:
     * 1. Redis Pub/Sub을 통해 모든 서버 인스턴스로 브로드캐스트
     * 2. 각 서버의 Subscriber가 로컬 SSE 클라이언트에게 전송
     * 3. DB 상태를 SENT로 업데이트
     *
     * ⚠️ 중복 방지: Redis로만 발행 (로컬 직접 전송 제거)
     * - Redis Subscriber가 모든 서버(자기 자신 포함)에 전송
     *
     * @param n 전송할 알림 엔티티
     */
    @Transactional(noRollbackFor = Exception.class)
    public void tryDeliver(Notification n) {
        if (n.getUserId() == null) {
            log.warn("userId가 null인 알림: id={}", n.getId());
            return;
        }

        boolean published = redisPublisher.publish(NotificationDto.Response.from(n));

        if (published) {
            n.markSent();   // 정상 송신
        } else {
            n.increaseRetry();  // 실패 → retry 증가
        }
        repository.save(n);

        log.debug("알림 Redis 발행 완료: id={}, userId={}", n.getId(), n.getUserId());
    }

    // ========== 알림 조회 ==========

    /**
     * 특정 사용자의 모든 알림 조회 (최신순)
     *
     * @param userId 조회할 사용자 ID
     * @return 알림 목록
     */
    @Transactional(readOnly = true)
    public List<NotificationDto.Response> getUserNotifications(Long userId) {
        List<Notification> notifications = repository.findByUserIdOrderByTimestampDesc(userId);
        return notifications.stream().map(NotificationDto.Response::from).toList();
    }

    /**
     * 읽지 않은 알림 수 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 읽지 않은 알림 수
     */
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return repository.countUnreadByUserId(userId);
    }

    // ========== 알림 상태 변경 ==========

    /**
     * 특정 알림 읽음 처리
     *
     * @param notificationId 읽음 처리할 알림 ID
     * @param userId 요청한 사용자 ID (권한 확인용)
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        repository.findById(notificationId).ifPresent(n -> {
            if (n.getUserId().equals(userId)) {
                if (n.getStatus() != DeliveryStatus.READ) {
                    n.markRead();
                }
                repository.save(n);
                log.debug("알림 읽음 처리: id={}, userId={}", notificationId, userId);
            }
        });
    }

    /**
     * 모든 알림 읽음 처리
     *
     * @param userId 요청한 사용자 ID
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications =
                repository.findByUserIdOrderByTimestampDesc(userId);

        notifications.forEach(n -> {
            if (n.getStatus() != DeliveryStatus.READ) {
                n.markRead();
            }
        });

        repository.saveAll(notifications);
        log.info("모든 알림 읽음 처리: userId={}, count={}", userId, notifications.size());
    }


    // ========== 알림 삭제 ==========

    /**
     * 특정 알림 삭제
     *
     * @param notificationId 삭제할 알림 ID
     * @param userId 요청한 사용자 ID (권한 확인용)
     */
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
     * Heartbeat 전송 (10초마다)
     * - 연결 유지 및 비활성 연결 정리
     */
    @Scheduled(fixedDelay = 10_000L)
    public void sendHeartbeat() {
        if (emitterManager.getConnectedUserCount() == 0) {
            return;
        }

        String timestamp = Instant.now().toString();
        Map<String, Integer> stats = emitterManager.sendHeartbeat(timestamp);

        // 통계는 SseEmitterManager 내부에서 로깅됨
    }

    @Transactional
    public void markEmailSent(Long id) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.markEmailSent();      // 엔티티의 비즈니스 메서드 호출

        repository.save(n);
    }


    /**
     * 오래된 알림 정리 (매일 새벽 2시)
     * - 30일 이상 지난 SENT 상태 알림 삭제
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupOldNotifications() {
        long threshold = System.currentTimeMillis() -
                (OLD_NOTIFICATION_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L);
        try {
            repository.deleteOldSentNotifications(threshold);
            log.info("오래된 알림 삭제 완료: {}일 전", OLD_NOTIFICATION_THRESHOLD_DAYS);
        } catch (Exception e) {
            log.error("오래된 알림 삭제 실패", e);
        }
    }

    /**
     * 재시도 초과 알림 처리 (매 시간 정각)
     * - retryCount >= 5인 알림 최종 삭제
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void handleFailedNotifications() {
        List<Notification> failed = repository.findFailedNotifications(MAX_RETRY_COUNT);
        if (failed.isEmpty()) return;

        for (Notification n : failed) {
            log.warn("알림 전송 최종 실패: id={}, userId={}, retryCount={}, title={}",
                    n.getId(), n.getUserId(), n.getRetryCount(), n.getTitle());
            repository.delete(n);
        }

        log.info("전송 실패 알림 처리 완료: count={}", failed.size());
    }

    public List<NotificationDto.Response> getUnsentNotifications() {
        List<Notification> unsentList = repository.findByStatus(DeliveryStatus.UNSENT);

        return unsentList.stream()
                .map(NotificationDto.Response::from)
                .collect(Collectors.toList());
    }

    // ========== 모니터링 ==========

    /**
     * SSE 연결 상태 조회
     * - 관리자용 모니터링 API에서 사용
     *
     * @return 연결 상태 정보
     */
    public Map<String, Object> getConnectionStatus() {
        return emitterManager.getConnectionStatus();
    }
}