package com.halo.core_bridge.api.schedule.jobposting.controller;

import com.halo.core_bridge.api.schedule.jobposting.model.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.service.JobPostingScheduleService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/jobpostings")
public class JobPostingScheduleController {

    private final JobPostingScheduleService service;

    @GetMapping
    public BaseResponse<List<JobPostingScheduleDto.Response>> getAll() {
        return BaseResponse.success(service.getAll());
    }

    @GetMapping("/{jobPostingId}")
    public BaseResponse<List<JobPostingScheduleDto.Response>> getByJob(@PathVariable Long jobPostingId) {
        return BaseResponse.success(service.getByJobPosting(jobPostingId));
    }

    @GetMapping("/detail/{scheduleId}")
    public BaseResponse<JobPostingScheduleDto.Response> get(@PathVariable Long scheduleId) {
        return BaseResponse.success(service.get(scheduleId));
    }

    @PostMapping
    public BaseResponse<JobPostingScheduleDto.Response> create(@RequestBody JobPostingScheduleDto.Request req) {
        return BaseResponse.success(service.create(req));
    }

    @PutMapping("/{scheduleId}")
    public BaseResponse<JobPostingScheduleDto.Response> update(@PathVariable Long scheduleId, @RequestBody JobPostingScheduleDto.Request req) {
        return BaseResponse.success(service.update(scheduleId, req));
    }

    @DeleteMapping("/{scheduleId}")
    public BaseResponse<String> delete(@PathVariable Long scheduleId) {
        service.delete(scheduleId);
        return BaseResponse.success("deleted");
    }
}
