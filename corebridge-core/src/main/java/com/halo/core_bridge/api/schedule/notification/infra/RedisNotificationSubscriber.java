package com.halo.core_bridge.api.schedule.notification.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Redis Pub/Sub 구독자
 * Redis로부터 알림을 수신하여 SSE로 전달하고 DB 상태 업데이트
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class RedisNotificationSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;

    // ✅ [중앙 집중식 SSE 연결 관리] NotificationService와 공유
    private final SseEmitterManager emitterManager;

    /**
     * Redis로부터 메시지 수신 시 호출되는 메서드
     *
     * 처리 흐름:
     * 1. Redis 메시지를 NotificationDto.Response로 역직렬화
     * 2. SseEmitterManager를 통해 해당 사용자에게 SSE 전송
     * 3. 전송 결과 로깅
     *
     * @param message Redis 메시지
     * @param pattern 구독 패턴 (현재는 미사용)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody());
            log.info("📨 [Redis Subscriber] 메시지 수신: {}", body);

            // JSON을 NotificationDto.Response로 역직렬화
            NotificationDto.Response notification = objectMapper.readValue(
                    body,
                    NotificationDto.Response.class
            );

            // ✅ SseEmitterManager를 통해 해당 사용자에게 알림 전송
            boolean delivered = emitterManager.sendToUser(
                    notification.getUserId(),
                    SseEmitter.event()
                            .name("notification")
                            .id(String.valueOf(notification.getId()))
                            .data(notification)
            );

            if (delivered) {
                log.info("✅ [Redis → SSE] 알림 전달 완료: id={}, userId={}, title={}",
                        notification.getId(), notification.getUserId(), notification.getTitle());
            } else {
                log.warn("⚠️ [Redis → SSE] 알림 전달 실패 (연결 없음): id={}, userId={}",
                        notification.getId(), notification.getUserId());
            }

        } catch (Exception e) {
            log.error("❌ [Redis Subscriber] 메시지 처리 실패: {}", e.getMessage(), e);
        }
    }
}
