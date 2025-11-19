package com.halo.core_bridge.api.schedule.notification.model.entity;

import com.halo.core_bridge.api.schedule.notification.model.enums.DeliveryStatus;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.users.model.UserRoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private int retryCount;
    private long timestamp;

    @PrePersist
    public void onCreate() {
        if (timestamp == 0) timestamp = Instant.now().toEpochMilli();
        if (status == null) status = DeliveryStatus.UNSENT;
    }

    public void markSent() { this.status = DeliveryStatus.SENT; }
    public void increaseRetry() { this.retryCount++; }

    public void markEmailSent() {
        this.status = DeliveryStatus.EMAIL_SENT;
    }

    public void markRead() {
        this.status = DeliveryStatus.READ;
    }

}
