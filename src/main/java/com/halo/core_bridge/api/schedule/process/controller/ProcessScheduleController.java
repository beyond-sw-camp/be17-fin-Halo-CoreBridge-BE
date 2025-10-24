package com.halo.core_bridge.api.schedule.process.controller;

import com.halo.core_bridge.api.schedule.process.model.dto.ProcessScheduleDto;
import com.halo.core_bridge.api.schedule.process.service.ProcessScheduleService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/process")
public class ProcessScheduleController {
    private final ProcessScheduleService service;

    @GetMapping
    public BaseResponse<List<ProcessScheduleDto.Response>> getAll() {
        return BaseResponse.success(service.getAll());
    }

    @GetMapping("/jobpostings/{jobPostingId}")
    public BaseResponse<List<ProcessScheduleDto.Response>> getByJob(@PathVariable Long jobPostingId) {
        return BaseResponse.success(service.getByJobPosting(jobPostingId));
    }

    @GetMapping("/{scheduleId}")
    public BaseResponse<ProcessScheduleDto.Response> get(@PathVariable Long scheduleId) {
        return BaseResponse.success(service.get(scheduleId));
    }

    @PostMapping
    public BaseResponse<ProcessScheduleDto.Response> create(@RequestBody ProcessScheduleDto.Request req) {
        return BaseResponse.success(service.create(req));
    }

    @PutMapping("/{scheduleId}")
    public BaseResponse<ProcessScheduleDto.Response> update(@PathVariable Long scheduleId, @RequestBody ProcessScheduleDto.Request req) {
        return BaseResponse.success(service.update(scheduleId, req));
    }

    @DeleteMapping("/{scheduleId}")
    public BaseResponse<String> delete(@PathVariable Long scheduleId) {
        service.delete(scheduleId);
        return BaseResponse.success("deleted");
    }
}
