package org.example.corebridgebatch.notification.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.corebridgebatch.notification.model.enums.DeliveryStatus;
import org.example.corebridgebatch.notification.model.enums.NotificationType;
import org.example.corebridgebatch.notification.model.enums.UserRoleType;

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

    @Transient
    private String email;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String message;

    @Setter
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
