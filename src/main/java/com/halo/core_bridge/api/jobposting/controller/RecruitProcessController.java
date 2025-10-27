package com.halo.core_bridge.api.jobposting.controller;

import com.halo.core_bridge.api.jobposting.service.RecruitProcessService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto.*;

@RestController
@RequestMapping("/api/recruiter/processes")
@RequiredArgsConstructor
public class RecruitProcessController {

    private final RecruitProcessService recruitProcessService;

    @PostMapping
    public ResponseEntity<BaseResponse<recruitProcesses>> createRecruitProcess(@RequestBody Create createRecruitProcess) {

        recruitProcessService.add(createRecruitProcess);

        recruitProcesses recruitProcesses =
                recruitProcessService.findAllRecruitProcessesByJobPostingId(createRecruitProcess.getJobPostingId());

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(recruitProcesses));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<recruitProcesses>> getRecruitProcesses(@RequestParam("recruit") Long jobPostingId) {

        recruitProcesses findRecruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(jobPostingId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(findRecruitProcesses));
    }

    @PatchMapping
    public ResponseEntity<BaseResponse<Object>> changeProcessOrder(@RequestBody ChangeOrder changeOrder) {

        recruitProcessService.changeOrder(changeOrder);
        recruitProcesses recruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(changeOrder.getJobPostingId());

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(recruitProcesses));
    }

    @PatchMapping("/{processId}")
    public ResponseEntity<BaseResponse<Object>> updateProcessName(@PathVariable Long processId ,@RequestBody Update updateRecruitProcess) {

        recruitProcessService.editRecruitProcess(processId, updateRecruitProcess);
        recruitProcesses recruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(updateRecruitProcess.getJobPostingId());

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(recruitProcesses));
    }
}
