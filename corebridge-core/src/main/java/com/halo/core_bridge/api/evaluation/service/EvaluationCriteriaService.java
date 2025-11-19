package com.halo.core_bridge.api.evaluation.service;

import com.halo.core_bridge.api.evaluation.model.entity.EvaluationCriteria;
import com.halo.core_bridge.api.evaluation.repository.EvaluationCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.halo.core_bridge.api.evaluation.model.dto.EvaluationDto.*;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaService {

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    public EvaluationCriteriaList findAll() {

        List<EvaluationCriteria> findALl = evaluationCriteriaRepository.findAllOderNoAsc();
        return EvaluationCriteriaList.from(findALl);
    }
}