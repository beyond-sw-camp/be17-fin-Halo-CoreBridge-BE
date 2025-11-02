package com.halo.core_bridge.api.schedule.notification.controller;

import com.halo.core_bridge.api.schedule.notification.service.NotificationSseEmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림", description = "SSE를 이용한 실시간 알림 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSseController {
    private final NotificationSseEmitterService sseService;

    @Operation(
            summary = "알림 구독",
            description = "SSE를 통해 실시간 알림을 구독합니다.",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "사용자 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "구독 성공",
                            content = @Content(
                                    mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)
                    )
            }
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam(name = "userId", required = false) Long userId) {
        if (userId == null) userId = 1L; // TODO 여기 수정
        return sseService.subscribe(userId);
    }
}
