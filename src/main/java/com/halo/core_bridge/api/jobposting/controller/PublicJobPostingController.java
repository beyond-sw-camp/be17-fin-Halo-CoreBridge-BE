package com.halo.core_bridge.api.jobposting.controller;

import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.service.JobPostingPublicService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class PublicJobPostingController {

    private final JobPostingPublicService jobPostingPublicService;

    @GetMapping
    public ResponseEntity<BaseResponse<Object>> getPublicJobPostingList() {

        PublicJobPostingDto.Jobs jobs = jobPostingPublicService.findAllJobs();
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(jobs));
    }
}
