package com.halo.core_bridge.api.schedule.jobprocess.controller;

import com.halo.core_bridge.api.schedule.jobprocess.model.dto.JobProcessScheduleDto;
import com.halo.core_bridge.api.schedule.jobprocess.service.JobProcessScheduleService;
import com.halo.core_bridge.common.model.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruiter/jobs/{jobPostingId}/schedules")
public class JobProcessScheduleController {

    private final JobProcessScheduleService service;

    /** 스케줄 생성 (공고별) */
    @PostMapping
    public ResponseEntity<BaseResponse<JobProcessScheduleDto.Response>> create(
            @PathVariable Long jobPostingId,
            @Valid @RequestBody JobProcessScheduleDto.Create req) {

        JobProcessScheduleDto.Response created = service.create(jobPostingId, req);

        return ResponseEntity
                .created(URI.create("/api/recruiter/jobs/" + jobPostingId + "/schedules/" + created.getId()))
                .body(BaseResponse.success(created));
    }

    /** 스케줄 수정 (공고별) */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<JobProcessScheduleDto.Response>> update(
            @PathVariable Long jobPostingId,
            @PathVariable Long id,
            @Valid @RequestBody JobProcessScheduleDto.Update req) {

        return ResponseEntity.ok(
                BaseResponse.success(service.update(id, jobPostingId, req))
        );
    }

    /** 스케줄 삭제 (공고별) */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable Long jobPostingId,
            @PathVariable Long id) {

        service.deleteByPosting(jobPostingId, id);
        return ResponseEntity.noContent().build();
    }

    /** 반복 일정 시리즈 전체 삭제 */
    @DeleteMapping("/{id}/series")
    public ResponseEntity<BaseResponse<Void>> deleteRecurringSeries(
            @PathVariable Long jobPostingId,
            @PathVariable Long id) {

        service.deleteRecurringSeries(jobPostingId, id);
        return ResponseEntity.noContent().build();
    }

    /** 스케줄 목록 조회 (공고별) */
    @GetMapping
    public ResponseEntity<BaseResponse<List<JobProcessScheduleDto.Response>>> list(
            @PathVariable Long jobPostingId) {

        return ResponseEntity.ok(
                BaseResponse.success(service.listByPosting(jobPostingId))
        );
    }

    /** 스케줄 상세 조회 (공고별) */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<JobProcessScheduleDto.Response>> get(
            @PathVariable Long jobPostingId,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                BaseResponse.success(service.getByPosting(jobPostingId, id))
        );
    }

    /** 스케줄 공유 */
    @PostMapping("/{id}/share")
    public ResponseEntity<BaseResponse<Void>> share(
            @PathVariable Long jobPostingId,
            @PathVariable Long id,
            @Valid @RequestBody JobProcessScheduleDto.ShareRequest req) {

        service.share(id, req);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    /** 일괄 공유 */
    @PostMapping("/share/bulk")
    public ResponseEntity<BaseResponse<Void>> bulkShare(
            @PathVariable Long jobPostingId,
            @Valid @RequestBody JobProcessScheduleDto.BulkShareRequest req) {

        service.bulkShare(req);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
