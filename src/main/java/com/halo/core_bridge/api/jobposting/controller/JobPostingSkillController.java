package com.halo.core_bridge.api.jobposting.controller;

import com.halo.core_bridge.api.jobposting.model.dto.TeckStackDto.TeckStackResponse;
import com.halo.core_bridge.api.jobposting.service.JobPostingSkillService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tech-stacks")
public class JobPostingSkillController {
    private final JobPostingSkillService jobPostingSkillService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<TeckStackResponse>>> getAllTechStacks() {
        List<TeckStackResponse> allTechStacks = jobPostingSkillService.getAllTechStacks();
        return ResponseEntity.ok(BaseResponse.success(allTechStacks));
    }
}
