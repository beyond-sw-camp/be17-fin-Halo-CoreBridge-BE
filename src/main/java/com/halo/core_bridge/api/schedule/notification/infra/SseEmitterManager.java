package com.halo.core_bridge.api.schedule.notification.infra;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class SseEmitterManager {

    private final Map<Long, Set<SseEmitter>> emittersByUser = new ConcurrentHashMap<>();

    /**
     * Emitter 등록
     */
    public void register(Long userId, SseEmitter emitter) {
        emittersByUser.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(emitter);
        log.debug("📡 Emitter 등록: userId={}, totalConnections={}", userId, getTotalConnectionCount());
    }

    /**
     * Emitter 제거
     */
    public void remove(Long userId, SseEmitter emitter) {
        Set<SseEmitter> emitters = emittersByUser.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                emittersByUser.remove(userId);
            }
        }
        log.debug("📡 Emitter 제거: userId={}, remainingConnections={}", userId, getTotalConnectionCount());
    }

    /**
     * 특정 유저에게 SSE 이벤트 전송
     */
    public boolean sendToUser(Long userId, SseEmitter.SseEventBuilder event) {
        Set<SseEmitter> emitters = emittersByUser.get(userId);
        if (emitters == null || emitters.isEmpty()) {
            return false;
        }

        boolean anyDelivered = false;
        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(event);
                anyDelivered = true;
            } catch (IOException e) {
                log.warn("💔 Emitter 전송 실패: userId={}, error={}", userId, e.getMessage());
            } catch (Exception e) {
                log.error("❌ Emitter 전송 중 예외: userId={}", userId, e);
            }
        }
        return anyDelivered;
    }

    /**
     * 특정 유저의 Emitter 목록 조회
     */
    public Set<SseEmitter> getEmitters(Long userId) {
        return emittersByUser.get(userId);
    }

    /**
     * 전체 연결 수 조회
     */
    public int getTotalConnectionCount() {
        return emittersByUser.values().stream()
                .mapToInt(Set::size)
                .sum();
    }

    /**
     * 연결된 유저 수 조회
     */
    public int getConnectedUserCount() {
        return emittersByUser.size();
    }

    /**
     * 특정 유저의 연결 수 조회
     */
    public int getUserConnectionCount(Long userId) {
        Set<SseEmitter> emitters = emittersByUser.get(userId);
        return emitters != null ? emitters.size() : 0;
    }

    /**
     * Heartbeat 전송 (모든 연결에 대해)
     */
    public Map<String, Integer> sendHeartbeat(String timestamp) {
        int sent = 0, success = 0, fail = 0;

        for (Map.Entry<Long, Set<SseEmitter>> entry : emittersByUser.entrySet()) {
            Long userId = entry.getKey();
            for (SseEmitter em : new ArrayList<>(entry.getValue())) {
                sent++;
                try {
                    em.send(SseEmitter.event()
                            .name("heartbeat")
                            .data(timestamp)
                            .comment("Keep-alive heartbeat"));
                    success++;
                } catch (Exception e) {
                    fail++;
                    log.warn("💔 Heartbeat 전송 실패: userId={}", userId);
                }
            }
        }

        log.info("💓 Heartbeat 전송 완료: users={}, total={}, success={}, fail={}",
                getConnectedUserCount(), sent, success, fail);

        return Map.of("sent", sent, "success", success, "fail", fail);
    }

    /**
     * 전체 연결 상태 조회
     */
    public Map<String, Object> getConnectionStatus() {
        return Map.of(
                "connectedUsers", getConnectedUserCount(),
                "totalConnections", getTotalConnectionCount(),
                "userConnections", emittersByUser.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey, e -> e.getValue().size()
                        ))
        );
    }
}