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

    // ========== SSE 구독 ==========

    /**
     * SSE 구독 엔드포인트
     * GET /api/notifications/subscribe
     */
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

    // ========== 알림 생성 및 전송 ==========

    /**
     * 알림 생성 및 전송
     * POST /api/notifications
     */
    @PostMapping
    public ResponseEntity<NotificationDto.Response> send(
            @AuthenticationPrincipal UserDto.Auth auth,
            @RequestBody NotificationDto.Request request) {

        if (auth == null) {
            // 부하테스트나 비로그인 요청용 기본값
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


    // ========== 알림 조회 ==========

    /**
     * 내 알림 목록 조회
     * GET /api/notifications
     */
    @GetMapping
    public ResponseEntity<List<NotificationDto.Response>> getMyNotifications(
            @AuthenticationPrincipal UserDto.Auth auth) {

        List<NotificationDto.Response> notifications = service.getUserNotifications(auth.getId());
        return ResponseEntity.ok(notifications);
    }

    /**
     * 읽지 않은 알림 수 조회
     * GET /api/notifications/unread/count
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserDto.Auth auth) {

        long count = service.getUnreadCount(auth.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    // ========== 알림 상태 변경 ==========

    /**
     * 특정 알림 읽음 처리
     * PATCH /api/notifications/{id}/read
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal UserDto.Auth auth,
            @PathVariable Long id) {

        service.markAsRead(id, auth.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * 모든 알림 읽음 처리
     * PATCH /api/notifications/read-all
     */
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal UserDto.Auth auth) {

        service.markAllAsRead(auth.getId());
        return ResponseEntity.ok().build();
    }

    // ========== 알림 삭제 ==========

    /**
     * 특정 알림 삭제
     * DELETE /api/notifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @AuthenticationPrincipal UserDto.Auth auth,
            @PathVariable Long id) {

        service.deleteNotification(id, auth.getId());
        return ResponseEntity.ok().build();
    }

    // ========== 모니터링 (관리자용) ==========

    /**
     * SSE 연결 상태 조회
     * GET /api/notifications/status
     *
     * 관리자만 접근 가능하도록 설정 필요
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getConnectionStatus() {
        return ResponseEntity.ok(service.getConnectionStatus());
    }
}
