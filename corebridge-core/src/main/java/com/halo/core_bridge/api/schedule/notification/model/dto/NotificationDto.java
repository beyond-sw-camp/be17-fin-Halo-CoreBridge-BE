package com.halo.core_bridge.api.schedule.notification.model.dto;

import com.halo.core_bridge.api.schedule.notification.model.entity.Notification;
import com.halo.core_bridge.api.schedule.notification.model.enums.DeliveryStatus;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.users.model.UserRoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class NotificationDto {

    @Getter
    public static class Request {
        @Setter
        private Long userId;
        private UserRoleType role;
        private NotificationType type;
        private String title;
        private String message;

        @Builder
        public Request(Long userId, UserRoleType role, NotificationType type,
                       String title, String message) {
            this.userId = userId;
            this.role = role;
            this.type = type != null ? type : NotificationType.SYSTEM;
            this.title = title;
            this.message = message;
        }

        public boolean isRoleBased() {
            return role != null && userId == null;
        }

        public void setSenderRole(UserRoleType role) {
            this.role = role;
        }

        public Notification toEntity(Long userId) {
            return Notification.builder()
                    .userId(userId)
                    .role(role)
                    .type(type)
                    .title(title)
                    .message(message)
                    .status(DeliveryStatus.UNSENT)
                    .timestamp(Instant.now().toEpochMilli())
                    .retryCount(0)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Response {
        private final Long id;
        private final Long userId;
        private final String userEmail;
        private final UserRoleType role;
        private final NotificationType type;
        private final String title;
        private final String message;
        private final DeliveryStatus status;
        private final long timestamp;

        /**
         *  기존 코드 호환용 오버로드 (email 없이도 생성 가능)
         */
        public static Response from(Notification entity) {
            return Response.builder()
                    .id(entity.getId())
                    .userId(entity.getUserId())
                    .userEmail(null)   // 기본값
                    .role(entity.getRole())
                    .type(entity.getType())
                    .title(entity.getTitle())
                    .message(entity.getMessage())
                    .status(entity.getStatus())
                    .timestamp(entity.getTimestamp())
                    .build();
        }

        /**
         *  email 포함 버전 (n8n 이메일 발송 등)
         */
        public static Response from(Notification entity, String email) {
            return Response.builder()
                    .id(entity.getId())
                    .userId(entity.getUserId())
                    .userEmail(email)
                    .role(entity.getRole())
                    .type(entity.getType())
                    .title(entity.getTitle())
                    .message(entity.getMessage())
                    .status(entity.getStatus())
                    .timestamp(entity.getTimestamp())
                    .build();
        }
    }
}
