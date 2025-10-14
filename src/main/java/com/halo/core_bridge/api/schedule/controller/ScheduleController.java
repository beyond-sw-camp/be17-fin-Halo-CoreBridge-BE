package com.halo.core_bridge.api.schedule.controller;

import com.halo.core_bridge.api.schedule.model.dto.ScheduleDto;
import com.halo.core_bridge.api.schedule.service.ScheduleService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /** 일정 등록 */
    @PostMapping
    public ResponseEntity<BaseResponse<ScheduleDto.RegistrationResult>> register(
            @RequestBody ScheduleDto.Registration dto,
            @AuthenticationPrincipal UserDto.Auth user) {

        if (user == null) throw new IllegalStateException("인증되지 않은 사용자입니다.");

        dto.setOwnerId(user.getId().toString());
        dto.setOriOwnerId(user.getId().toString());

        ScheduleDto.RegistrationResult result = scheduleService.register(dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    /** 일정 목록 조회 */
    @GetMapping
    public ResponseEntity<BaseResponse<List<ScheduleDto.Response>>> getSchedules(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @AuthenticationPrincipal UserDto.Auth user) {

        if (user == null) throw new IllegalStateException("인증되지 않은 사용자입니다.");

        List<ScheduleDto.Response> result =
                scheduleService.getSchedules(user.getId().toString(), year, month);

        return ResponseEntity.ok(BaseResponse.success(result));
    }

    /** 단일 일정 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ScheduleDto.Response>> getSchedule(@PathVariable Long id) {
        ScheduleDto.Response result = scheduleService.getSchedule(id);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    /** 일정 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleDto.Update dto) {

        scheduleService.updateSchedule(id, dto);
        return ResponseEntity.ok(BaseResponse.success("일정이 수정되었습니다."));
    }

    /** 일정 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(BaseResponse.success("일정이 삭제되었습니다."));
    }

    /** 일정 공유 */
    @PostMapping("/{id}/share")
    public ResponseEntity<BaseResponse<List<ScheduleDto.CloneResult>>> shareSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleDto.ShareRequest request,
            @AuthenticationPrincipal UserDto.Auth user) {

        if (user == null) throw new IllegalStateException("인증되지 않은 사용자입니다.");

        List<ScheduleDto.CloneResult> result =
                scheduleService.shareScheduleToMultiple(id, user.getId().toString(), request.getTargetUserIds());

        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
