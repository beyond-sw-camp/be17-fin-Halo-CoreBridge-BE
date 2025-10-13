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

    @OneToMany(mappedBy = "jobPosting", fetch = FetchType.LAZY)
    private List<JobPostingSkill> skills = new ArrayList<>();

    public void addSkill(JobPostingSkill skill) {
        this.skills.add(skill);
        skill.setJobPosting(this);
    }
}
