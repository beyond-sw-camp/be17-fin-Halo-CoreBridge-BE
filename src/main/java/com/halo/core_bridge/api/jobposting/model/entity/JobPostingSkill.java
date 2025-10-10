package com.halo.core_bridge.api.jobposting.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingSkill extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }
}