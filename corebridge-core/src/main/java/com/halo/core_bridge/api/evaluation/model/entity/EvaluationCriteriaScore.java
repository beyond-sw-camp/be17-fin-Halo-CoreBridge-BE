package com.halo.core_bridge.api.evaluation.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationCriteriaScore extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Evaluation evaluation;

    @ManyToOne(fetch =  FetchType.LAZY)
    private EvaluationCriteria criteria;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, length = 2000)
    private String comment;
}
