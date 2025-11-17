package org.example.batch.service;

import com.halo.core_bridge.api.schedule.notification.infra.RedisNotificationPublisher;
import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import com.halo.core_bridge.api.schedule.notification.model.entity.Notification;
import com.halo.core_bridge.api.schedule.notification.model.enums.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Batch 전용 서비스
 * 
 * 역할:
 * - 미전송 알림을 Redis로 재발행
 * - 재시도 횟수 관리
 * - 전송 상태 업데이트
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationBatchService {
    
    private final RedisNotificationPublisher redisPublisher;
    private static final int MAX_RETRY = 5;
    
    /**
     * 알림 재전송 시도
     * 
     * @param notification 재전송할 알림
     */
    public void tryDeliver(Notification notification) {
        try {
            // DTO 변환
            NotificationDto.Response dto = NotificationDto.Response.builder()
                    .id(notification.getId())
                    .userId(notification.getUserId())
                    .type(notification.getType())
                    .title(notification.getTitle())
                    .message(notification.getMessage())
                    .url(notification.getUrl())
                    .timestamp(notification.getTimestamp())
                    .isRead(notification.getIsRead())
                    .build();
            
            // Redis로 재발행
            boolean published = redisPublisher.publish(dto);
            
            if (published) {
                // 성공 시 상태 업데이트
                notification.setDeliveryStatus(DeliveryStatus.SENT);
                notification.setRetryCount(notification.getRetryCount() + 1);
                
                log.info("✅ [Batch] 알림 재전송 성공: id={}, userId={}, retry={}",
                        notification.getId(), notification.getUserId(), notification.getRetryCount());
            } else {
                // 실패 시 재시도 횟수만 증가
                notification.setRetryCount(notification.getRetryCount() + 1);
                
                if (notification.getRetryCount() >= MAX_RETRY) {
                    notification.setDeliveryStatus(DeliveryStatus.FAILED);
                    log.error("❌ [Batch] 알림 재전송 최종 실패: id={}, userId={}, retry={}",
                            notification.getId(), notification.getUserId(), notification.getRetryCount());
                } else {
                    log.warn("⚠️ [Batch] 알림 재전송 실패 (재시도 예정): id={}, retry={}",
                            notification.getId(), notification.getRetryCount());
                }
            }
            
        } catch (Exception e) {
            notification.setRetryCount(notification.getRetryCount() + 1);
            
            if (notification.getRetryCount() >= MAX_RETRY) {
                notification.setDeliveryStatus(DeliveryStatus.FAILED);
            }
            
            log.error("❌ [Batch] 알림 재전송 예외 발생: id={}, error={}",
                    notification.getId(), e.getMessage(), e);
        }
    }
}
