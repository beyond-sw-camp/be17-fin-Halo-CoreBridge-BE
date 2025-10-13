package com.halo.core_bridge.api.jobposting.controller;


import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.service.JobPostingService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-postings")
public class JobPostingController {
    private final JobPostingService jobPostingService;

    //채용공고 등록
    @PostMapping
    public ResponseEntity<BaseResponse<String>> createJobPosting(
            @RequestBody @Validated JobPostingDto.CreateRequest request) {
        Long savedId = jobPostingService.save(request);
        // 생성된 리소스의 URI 생성
        URI location = URI.create("/job-postings/" + savedId);

        // 201 Created + Location 헤더 포함
        return ResponseEntity
                .created(location)
                .body(BaseResponse.success("저장완료"));
    }

    //채용공고 전체 목록 조회
    @GetMapping
    public ResponseEntity
            <BaseResponse<List<JobPostingDto.ListResponse>>> getAllJobPostings() {
        List<JobPostingDto.ListResponse> list = jobPostingService.getList();
        return ResponseEntity.ok(BaseResponse.success(list));
    }

    //채용공고 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostingDto.DetailResponse>> getJobPostingDetail(
            @PathVariable Long id
    ) {
        JobPostingDto.DetailResponse detail = jobPostingService.getDetail(id);

        return ResponseEntity
                .ok(BaseResponse.success(detail)); // HTTP 200 OK
    }
}
