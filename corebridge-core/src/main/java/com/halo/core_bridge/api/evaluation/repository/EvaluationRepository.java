package com.halo.core_bridge.api.evaluation.repository;

import com.halo.core_bridge.api.evaluation.model.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByAssignmentId(Long assignmentId);

    boolean existsEvaluationByAssignment_Id(Long assignmentId);
}
