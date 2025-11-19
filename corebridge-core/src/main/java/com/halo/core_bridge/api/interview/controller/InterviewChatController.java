package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.service.InterviewService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.halo.core_bridge.api.interview.model.dto.InterviewChatDto.ResumeLoadForInfo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview/chat")
public class InterviewChatController {

    private final InterviewService interviewService;

    @GetMapping("/{interviewId}/applicant")
    public ResponseEntity<BaseResponse<ResumeLoadForInfo>> getResumeLoadData(@PathVariable("interviewId") Long interviewId) {

        ResumeLoadForInfo resumeLoadInfo = interviewService.findResumeLoadInfo(interviewId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(resumeLoadInfo));
    }
}
