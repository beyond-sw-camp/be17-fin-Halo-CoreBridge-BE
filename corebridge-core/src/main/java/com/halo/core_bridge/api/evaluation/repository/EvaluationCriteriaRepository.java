package com.halo.core_bridge.api.evaluation.repository;

import com.halo.core_bridge.api.evaluation.model.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Long> {

    @Query("select e from EvaluationCriteria e order by e.orderNo asc")
    List<EvaluationCriteria> findAllOderNoAsc();
}
