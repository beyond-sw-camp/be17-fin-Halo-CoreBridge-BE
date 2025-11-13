package com.halo.core_bridge.api.schedule.notification.controller;

import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    // =====================================================
    // 1. SSE 구독
    // =====================================================

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @AuthenticationPrincipal UserDto.Auth auth,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

        UserRoleType roleEnum = null;
        try {
            roleEnum = UserRoleType.valueOf(auth.getRole().toUpperCase());
        } catch (Exception ignored) {}

        return service.subscribe(auth.getId(), roleEnum, lastEventId);
    }

    // =====================================================
    // 2. 알림 생성 및 발송
    // =====================================================

    @PostMapping
    public ResponseEntity<NotificationDto.Response> send(
            @AuthenticationPrincipal UserDto.Auth auth,
            @RequestBody NotificationDto.Request request) {

        if (auth == null) { // 부하테스트
            request.setUserId(1L);
            request.setSenderRole(UserRoleType.ROLE_ADMIN);
        } else {
            UserRoleType roleEnum = null;
            try {
                roleEnum = UserRoleType.valueOf(auth.getRole().toUpperCase());
            } catch (Exception ignored) {}
            request.setSenderRole(roleEnum);
            request.setUserId(auth.getId());
        }

        return ResponseEntity.ok(service.createAndDispatch(request));
    }

    // =====================================================
    // 3. n8n 콜백: 이메일 발송 성공
    // =====================================================

    @PostMapping("/hooks/n8n/email-sent/{id}")
    public ResponseEntity<Void> markEmailSent(@PathVariable Long id) {
        service.markEmailSent(id);
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // 4. 조회
    // =====================================================

    @GetMapping
    public ResponseEntity<List<NotificationDto.Response>> getMyNotifications(
            @AuthenticationPrincipal UserDto.Auth auth) {
        return ResponseEntity.ok(service.getUserNotifications(auth.getId()));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserDto.Auth auth) {
        long count = service.getUnreadCount(auth.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    /**
     * 미전송(UNSENT) 데이터 조회 → n8n (웹훅 트리거)
     */
    @GetMapping("/unsent")
    public ResponseEntity<List<NotificationDto.Response>> getUnsentNotifications() {
        return ResponseEntity.ok(service.getUnsentNotifications());
    }

    /**
     * 이메일 대상 조회 (UNSENT + SENT but unread)
     * GET /api/notifications/email-targets
     */
    @GetMapping("/email-targets")
    public ResponseEntity<List<NotificationDto.Response>> getEmailTargets() {
        return ResponseEntity.ok(service.getEmailTargetNotifications());
    }

    // =====================================================
    // 5. 상태 변경
    // =====================================================

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal UserDto.Auth auth,
            @PathVariable Long id) {
        service.markAsRead(id, auth.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal UserDto.Auth auth) {
        service.markAllAsRead(auth.getId());
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // 6. 삭제
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @AuthenticationPrincipal UserDto.Auth auth,
            @PathVariable Long id) {
        service.deleteNotification(id, auth.getId());
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // 7. SSE 모니터링
    // =====================================================

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getConnectionStatus() {
        return ResponseEntity.ok(service.getConnectionStatus());
    }
}

