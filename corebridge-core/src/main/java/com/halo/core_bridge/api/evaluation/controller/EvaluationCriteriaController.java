package com.halo.core_bridge.api.evaluation.controller;

import com.halo.core_bridge.api.evaluation.model.dto.EvaluationDto;
import com.halo.core_bridge.api.evaluation.service.EvaluationCriteriaService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviewer/evaluation-criteria")
public class EvaluationCriteriaController {

    private final EvaluationCriteriaService evaluationCriteriaService;

    @GetMapping
    public ResponseEntity<BaseResponse<EvaluationDto.EvaluationCriteriaList>> getAll() {

        EvaluationDto.EvaluationCriteriaList findCriteriaTemplates = evaluationCriteriaService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(findCriteriaTemplates));
    }
}