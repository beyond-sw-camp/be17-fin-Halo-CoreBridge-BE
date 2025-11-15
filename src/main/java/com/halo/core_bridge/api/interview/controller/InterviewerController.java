package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.model.dto.InterviewerDto;
import com.halo.core_bridge.api.interview.service.InterviewerService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviewers")
public class InterviewerController {

    private final InterviewerService interviewerService;

    @GetMapping
    public ResponseEntity<BaseResponse<Object>> getInterviewsByJobPosting(@RequestParam Long jobPostingId) {

        InterviewerDto.InterviewerList interviewers = interviewerService.findInterviewersByJobPostingId(jobPostingId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(interviewers));
    }
}
