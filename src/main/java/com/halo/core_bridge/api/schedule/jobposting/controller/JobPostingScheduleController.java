package com.halo.core_bridge.api.schedule.jobposting.controller;

import com.halo.core_bridge.api.schedule.jobposting.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.service.JobPostingScheduleService;
import com.halo.core_bridge.common.model.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/jobpostings")
public class JobPostingScheduleController {

    private final JobPostingScheduleService service;

    @PostMapping
    public ResponseEntity<BaseResponse<JobPostingScheduleDto.Response>> create(
            @Valid @RequestBody JobPostingScheduleDto.Create req) {

        JobPostingScheduleDto.Response created = service.create(req);
        return ResponseEntity
                .created(URI.create("/api/schedules/jobpostings/" + created.getId()))
                .body(BaseResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostingScheduleDto.Response>> update(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingScheduleDto.Update req) {

        return ResponseEntity
                .ok(BaseResponse.success(service.update(id, req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();     // BaseResponse 필요 없음
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<JobPostingScheduleDto.Response>>> list() {
        return ResponseEntity
                .ok(BaseResponse.success(service.list()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostingScheduleDto.Response>> get(@PathVariable Long id) {
        return ResponseEntity
                .ok(BaseResponse.success(service.get(id)));
    }

    @GetMapping("/calendar")
    public ResponseEntity<BaseResponse<Map<String, List<JobPostingScheduleDto.CalendarItem>>>> calendar(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity
                .ok(BaseResponse.success(service.calendar(year, month)));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<BaseResponse<String>> share(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingScheduleDto.ShareRequest req) {

        log.info("scheduleId={}, userIds={}", id, req.getUserIds());

        service.share(id, req);

        return ResponseEntity.ok(BaseResponse.success("공유 완료"));
    }

    @PostMapping("/share")
    public ResponseEntity<BaseResponse<Void>> bulkShare(
            @Valid @RequestBody JobPostingScheduleDto.BulkShareRequest req) {

        service.bulkShare(req);
        return ResponseEntity.ok(BaseResponse.success(null));
    }



}
