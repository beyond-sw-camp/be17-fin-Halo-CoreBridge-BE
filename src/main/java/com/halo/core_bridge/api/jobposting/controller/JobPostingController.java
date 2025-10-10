package com.halo.core_bridge.api.jobposting.controller;


import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.service.JobPostingService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-postings")
public class JobPostingController {
    private final JobPostingService jobPostingService;

    //채용공고 등록
    @PostMapping
    public BaseResponse<JobPostingDto.DetailResponse> createJobPosting(
            @RequestBody JobPostingDto.CreateRequest request
    ) {
       return BaseResponse.success(jobPostingService.save(request));
    }

    //채용공고 전체 목록 조회
    @GetMapping
    public BaseResponse<List<JobPostingDto.ListResponse>> getAllJobPostings() {
        return BaseResponse.success(jobPostingService.getList());
    }

    //채용공고 상세 조회
    @GetMapping("/{id}")
    public BaseResponse<JobPostingDto.DetailResponse> getJobPostingDetail(@PathVariable Long id) {
        return BaseResponse.success(jobPostingService.getDetail(id));
    }
}
