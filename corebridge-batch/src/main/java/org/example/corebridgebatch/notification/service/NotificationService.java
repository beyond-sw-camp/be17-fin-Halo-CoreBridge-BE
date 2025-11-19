package org.example.corebridgebatch.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.corebridgebatch.notification.model.entity.Notification;
import org.example.corebridgebatch.notification.model.enums.DeliveryStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    private static final int MAX_RETRY_COUNT = 5;

    @Transactional
    public void tryDeliver(Notification notification) {

        // 재시도 초과 → 배치에서는 건너뛰기
        if (notification.getRetryCount() >= MAX_RETRY_COUNT) {
            notification.setStatus(DeliveryStatus.UNSENT);
            log.warn("❌ 최대 재시도 초과 — 건너뜀: id={}", notification.getId());
            return;
        }

        try {
            boolean success = sendNotification(notification);

            if (success) {
                notification.markEmailSent();  // EMAIL_SENT 상태로
                log.info("✅ 이메일 전송 성공: id={}, email={}",
                        notification.getId(), notification.getEmail());
            } else {
                notification.increaseRetry();
                notification.setStatus(DeliveryStatus.UNSENT);
                log.warn("⚠️ 이메일 전송 실패 — 다음 배치에서 재시도: id={}, retry={}",
                        notification.getId(), notification.getRetryCount());
            }

        } catch (Exception e) {
            notification.increaseRetry();
            notification.setStatus(DeliveryStatus.UNSENT);
            log.error("🚨 전송 중 예외 — 다음 배치에서 재시도: id={}",
                    notification.getId(), e);
        }
    }

    private boolean sendNotification(Notification notification) {
        try {
            sendEmail(notification);
            return true;
        } catch (Exception e) {
            log.error("전송 실패: {}", e.getMessage());
            return false;
        }
    }

    private void sendEmail(Notification notification) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(notification.getEmail());
        mail.setSubject(notification.getTitle());
        mail.setText(notification.getMessage());

        try {
            mailSender.send(mail);
        } catch (Exception e) {
            log.error("📧 이메일 전송 실패: email={}, id={}",
                    notification.getEmail(), notification.getId(), e);
            throw e;
        }
    }
}

