package com.halo.core_bridge.api.evaluation.controller;

import com.halo.core_bridge.api.evaluation.model.dto.EvaluationDto;
import com.halo.core_bridge.api.evaluation.service.EvaluationService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviewer/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> addEvaluation(@RequestBody EvaluationDto.Create createEvaluation,
                                                              @AuthenticationPrincipal UserDto.Auth auth) {

        evaluationService.save(createEvaluation, auth.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("평가 완료"));
    }
}