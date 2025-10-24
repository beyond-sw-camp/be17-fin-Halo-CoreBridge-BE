package com.halo.core_bridge.api.jobposting.controller;

import com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto;
import com.halo.core_bridge.api.jobposting.service.RecruitProcessService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RecruitProcessController {

    private final RecruitProcessService recruitProcessService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createRecruitProcess(RecruitProcessDto.Create createRecruitProcessDto) {

        recruitProcessService.add(createRecruitProcessDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("채용 프로세스 추가 완료"));

    }
}
