package com.halo.core_bridge.api.schedule.notification.model.dto;

import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationPayload {
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private String jobTitle; // optional
    private String link;     // optional
    private long timestamp;
}
