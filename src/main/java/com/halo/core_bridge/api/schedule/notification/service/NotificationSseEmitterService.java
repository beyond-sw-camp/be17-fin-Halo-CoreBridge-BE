package com.halo.core_bridge.api.schedule.notification.service;

import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 60 * 1000; // 1h
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        try {
            emitter.send(SseEmitter.event().name("connected").data("connected"));
        } catch (IOException ignored) {}
        return emitter;
    }

    public void sendToUser(Long userId, NotificationPayload payload) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) { return; }
        try {
            emitter.send(SseEmitter.event().name("notification").data(payload));
        } catch (IOException e) {
            emitters.remove(userId);
        }
    }

    public void broadcast(NotificationPayload payload) {
        emitters.forEach((id, emitter) -> {
            try { emitter.send(SseEmitter.event().name("notification").data(payload)); }
            catch (IOException e) { emitters.remove(id); }
        });
    }
}
