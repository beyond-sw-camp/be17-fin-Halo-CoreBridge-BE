package com.halo.core_bridge.api.management.controller;

import com.halo.core_bridge.api.management.model.dto.ManagementDto;
import com.halo.core_bridge.api.management.service.ManagementService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{jobPostingId}/management")
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping
    public ResponseEntity<BaseResponse<ManagementDto>> getManagement(@PathVariable Long jobPostingId) {

        ManagementDto result = managementService.getManagement(jobPostingId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @PatchMapping("/{resumeId}/process/{processId}")
    public ResponseEntity<BaseResponse<String>> updateProcess(
            @PathVariable Long resumeId,
            @PathVariable Long processId
    ) {
        managementService.moveApplicantProcess(resumeId, processId);
        return ResponseEntity.ok(BaseResponse.success("단계 변경 완료"));
    }
}
