package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.service.InterviewService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruiter/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createInterview(@RequestBody InterviewDto.Create interviewRequest) {

        interviewService.save(interviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("면접을 등록하였습니다."));
    }
}
