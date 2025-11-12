package com.halo.core_bridge.api.jobposting.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import com.halo.core_bridge.common.model.ColorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitProcess extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer orderIdx;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ColorCode colorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPosting_id")
    private JobPosting jobPosting;

    public void updateRecruitProcess(String name, ColorCode colorCode) {
        this.name = name;
        this.colorCode = colorCode;
    }
}
