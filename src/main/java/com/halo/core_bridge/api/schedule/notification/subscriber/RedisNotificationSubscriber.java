package com.halo.core_bridge.api.schedule.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationPayload;
import com.halo.core_bridge.api.schedule.notification.service.NotificationSseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisNotificationSubscriber implements MessageListener {

    private final NotificationSseEmitterService emitterService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            log.info("📩 Redis Notification Received: {}", json);

            NotificationPayload payload = mapper.readValue(json, NotificationPayload.class);

            // 현재는 전체 broadcast (나중에 userId 매핑 로직 들어갈 예정)
            emitterService.broadcast(payload);

            log.info("✅ Notification broadcast 완료: {}", payload.getTitle());
        } catch (Exception e) {
            log.error("❌ Redis Notification 처리 중 오류 발생", e);
        }
    }
}
