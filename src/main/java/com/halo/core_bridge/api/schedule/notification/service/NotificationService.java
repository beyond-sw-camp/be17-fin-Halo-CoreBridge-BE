package com.halo.core_bridge.api.schedule.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationPayload;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic notificationTopic;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishNotification(Long userId,
                                    NotificationType type,
                                    String message,
                                    String jobTitle,
                                    String link) {
        try {
            NotificationPayload payload = NotificationPayload.builder()
                    .type(type)
                    .title(resolveTitle(type))
                    .message(message)
                    .jobTitle(jobTitle)
                    .link(link)
                    .timestamp(Instant.now().toEpochMilli())
                    .build();

            String json = objectMapper.writeValueAsString(payload);
            stringRedisTemplate.convertAndSend(notificationTopic.getTopic(), json);
            log.info("📤 Redis Publish: topic={}, payload={}", notificationTopic.getTopic(), json);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish notification", e);
        }
    }

    private String resolveTitle(NotificationType type) {
        return switch (type) {
            case JOB_SCHEDULE_CREATED -> "공고 일정 등록";
            case JOB_SCHEDULE_UPDATED -> "공고 일정 수정";
            case JOB_SCHEDULE_DELETED -> "공고 일정 삭제";
            case PROCESS_SCHEDULE_CREATED -> "프로세스 일정 등록";
            case PROCESS_SCHEDULE_UPDATED -> "프로세스 일정 수정";
            case PROCESS_SCHEDULE_DELETED -> "프로세스 일정 삭제";
            default -> "알림";
        };
    }
}
