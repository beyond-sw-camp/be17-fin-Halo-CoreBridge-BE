package com.halo.core_bridge.api.schedule.notification.infra;

import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Redis Pub/Sub 발행자
 * 알림을 Redis 채널로 발행
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class RedisNotificationPublisher {

    private final RedisTemplate<String, Object> notificationRedisTemplate;
    private final ChannelTopic notificationTopic;

    /**
     * Redis 채널에 알림 발행
     *
     * @param notification 발행할 알림
     * @return 발행 성공 여부
     */
    public boolean publish(NotificationDto.Response notification) {
        try {
            notificationRedisTemplate.convertAndSend(
                    notificationTopic.getTopic(),
                    notification
            );

            log.info("📤 [Redis Publisher] 알림 발행: id={}, userId={}, title={}",
                    notification.getId(), notification.getUserId(), notification.getTitle());

            return true;

        } catch (Exception e) {
            log.error("❌ [Redis Publisher] 발행 실패: id={}, error={}",
                    notification.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 여러 알림을 한 번에 발행
     */
    public void publishBatch(List<NotificationDto.Response> notifications) {
        int successCount = 0;
        int failCount = 0;

        for (NotificationDto.Response notification : notifications) {
            if (publish(notification)) {
                successCount++;
            } else {
                failCount++;
            }
        }

        log.info("📤 [Redis Publisher] 배치 발행 완료: success={}, fail={}", successCount, failCount);
    }
}
