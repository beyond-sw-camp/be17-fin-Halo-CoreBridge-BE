package com.halo.core_bridge.api.resume.controller;

import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.model.dto.ResumeResponseDto;
import com.halo.core_bridge.api.resume.service.ResumeService;
import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/register/{jobPostingId}")
    public ResponseEntity<BaseResponse<Long>> register(
            @RequestBody ResumeDto.Create requestDto,
            @PathVariable Long jobPostingId,
            @RequestParam Long userId) {

        Long resumeId = resumeService.register(requestDto, userId, jobPostingId);

        return ResponseEntity.ok(
                BaseResponse.success(resumeId)
        );
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<BaseResponse<ResumeResponseDto.Detail>> read(
            @PathVariable Long resumeId) {

        ResumeResponseDto.Detail response = resumeService.read(resumeId);

        return ResponseEntity.ok(
                BaseResponse.success(response)
        );
    }

    @GetMapping("/list/paged")
    public ResponseEntity<BaseResponse<Slice<ResumeResponseDto.Detail>>> list(
            @PageableDefault(size = 10, sort = "applied_at", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<ResumeResponseDto.Detail> response = resumeService.list(pageable);

        return ResponseEntity.ok(
                BaseResponse.success(response)
        );
    }

    @PutMapping("/{resumeId}")
    public ResponseEntity<BaseResponse<Long>> update(
            @PathVariable Long resumeId,
            @RequestBody ResumeDto.Update requestDto,
            @RequestParam Long userId) {

        Long updatedResumeId = resumeService.update(resumeId, requestDto);

        return ResponseEntity.ok(
                BaseResponse.success(updatedResumeId)
        );
    }

    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<BaseResponse<Void>> deleted(
            @PathVariable Long resumeId,
            @RequestParam Long userId) {

        resumeService.deleted(resumeId);

        return ResponseEntity.ok(
                BaseResponse.success(null)
        );
    }
}