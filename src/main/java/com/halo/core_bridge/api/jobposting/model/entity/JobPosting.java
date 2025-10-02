package com.halo.core_bridge.api.jobposting.model.entity;

import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime applyStartDate;
    private LocalDateTime applyEndDate;
    private LocalDateTime hireEndDate;
    private int minExperience;
    private int maxExperience;
    private String employmentType;
    private String careerType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
}
