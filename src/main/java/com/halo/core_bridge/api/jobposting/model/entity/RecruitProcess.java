package com.halo.core_bridge.api.jobposting.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecruitProcess extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int headCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;
}
