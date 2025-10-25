package com.halo.core_bridge.api.schedule.notification.controller;

import com.halo.core_bridge.api.schedule.notification.service.NotificationSseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSseController {
    private final NotificationSseEmitterService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam(name = "userId", required = false) Long userId) {
        if (userId == null) userId = 1L; // TODO 여기 수정
        return sseService.subscribe(userId);
    }
}
