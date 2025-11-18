package org.example.corebridgebatch.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.corebridgebatch.notification.model.entity.Notification;
import org.example.corebridgebatch.notification.model.enums.DeliveryStatus;
import org.example.corebridgebatch.notification.repository.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Batch 전용 NotificationService
 * - SSE, Controller 등 Web 관련 기능 제거
 * - 알림 전송 및 상태 관리만 담당
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    private static final int MAX_RETRY_COUNT = 5;

    /**
     * 알림 전송 시도
     * Batch에서 호출
     */
    @Transactional
    public void tryDeliver(Notification notification) {
        try {
            // 재시도 횟수 체크
            if (notification.getRetryCount() >= MAX_RETRY_COUNT) {
                log.warn("❌ 최대 재시도 횟수 초과: id={}, retryCount={}",
                        notification.getId(), notification.getRetryCount());
                notification.setStatus(DeliveryStatus.UNSENT);
                return;
            }

            // 실제 전송 (이메일, Slack, Kafka 등)
            boolean success = sendNotification(notification);

            if (success) {
                notification.setStatus(DeliveryStatus.SENT);
                log.info("✅ 알림 전송 성공: id={}, userId={}",
                        notification.getId(), notification.getUserId());
            } else {
                notification.setStatus(DeliveryStatus.UNSENT);
                notification.increaseRetry();
                log.warn("⚠️ 알림 전송 실패 (재시도 예정): id={}, retryCount={}",
                        notification.getId(), notification.getRetryCount());
            }

        } catch (Exception e) {
            log.error("❌ 알림 전송 중 예외 발생: id={}", notification.getId(), e);
            notification.setStatus(DeliveryStatus.UNSENT);
            notification.increaseRetry();
        }
    }

    /**
     * 실제 알림 전송 로직
     * - 이메일, Redis Pub/Sub, Kafka 등
     */
    private boolean sendNotification(Notification notification) {
        try {
            // 방법 1: 이메일 전송
            sendEmail(notification);

            // 방법 3: Kafka (필요시)

            return true;

        } catch (Exception e) {
            log.error("전송 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 이메일 전송
     */
    private void sendEmail(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(notification.getTitle());


            mailSender.send(message);


        } catch (Exception e) {
            log.error("이메일 전송 실패", e);
            throw e;
        }
    }
}
