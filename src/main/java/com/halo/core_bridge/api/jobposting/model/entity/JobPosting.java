package com.halo.core_bridge.api.jobposting.model.entity;

import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDateTime applyStartDate;
    private LocalDateTime applyEndDate;
    private LocalDateTime hireEndDate;

    @Column(nullable = true)
    private int minExperience;

    @Column(nullable = true)
    private int maxExperience;

    private String employmentType;
    private String careerType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToMany(
            mappedBy = "jobPosting",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,       // 부모 저장/수정 시 자식 함께 반영
            orphanRemoval = true             // 부모에서 제거되면 고아 삭제
    )
    private List<JobPostingSkill> skills = new ArrayList<>();
}
