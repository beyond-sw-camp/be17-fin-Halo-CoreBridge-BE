package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.admin.model.AdminDto;
import com.halo.core_bridge.api.interview.model.dto.InterviewerDto;
import com.halo.core_bridge.api.interview.service.InterviewerService;
import com.halo.core_bridge.api.users.service.UserService;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse<Object>> getInterviewsByJobPosting(@RequestParam Long jobPostingId) {

        InterviewerDto.InterviewerList interviewers = interviewerService.findInterviewersByJobPostingId(jobPostingId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(interviewers));
    }

    @GetMapping("/jobPosting")
    public ResponseEntity<BaseResponse<Object>> getInterviewers(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(name = "search", required = false) String keyword) {

        AdminDto.AccountInfiniteList findAccounts = userService.findInfiniteAccounts("면접관", page, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(findAccounts));
    }
}
