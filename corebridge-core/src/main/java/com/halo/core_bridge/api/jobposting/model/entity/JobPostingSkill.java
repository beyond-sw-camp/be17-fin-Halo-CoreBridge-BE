package com.halo.core_bridge.api.jobposting.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingSkill extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TechStack name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private JobPosting jobPosting;

}