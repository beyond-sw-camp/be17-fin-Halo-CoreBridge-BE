package com.halo.core_bridge.api.evaluation.model.entity;

import com.halo.core_bridge.api.interview.model.entity.InterviewAssignment;
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
public class Evaluation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private InterviewAssignment assignment;   // interview_assignment.id

    private Double avgScore;

    @Column(length = 2000)
    private String overallComment;
}
