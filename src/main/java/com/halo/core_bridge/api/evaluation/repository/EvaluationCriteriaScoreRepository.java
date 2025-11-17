package com.halo.core_bridge.api.evaluation.repository;

import com.halo.core_bridge.api.evaluation.model.entity.EvaluationCriteriaScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationCriteriaScoreRepository extends JpaRepository<EvaluationCriteriaScore, Long> {
}
