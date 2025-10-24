package com.halo.core_bridge.api.jobposting.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ex) applied, interview1, final ...
    @Column(nullable = false)
    private String code;

    // ex) 지원 완료, 1차 면접 ...
    @Column(nullable = false)
    private String name;

    // 칸반보드 순서
    @Column(name = "order_index")
    private Integer orderIndex;

    // jobPosting과 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPosting_id")
    private JobPosting jobPosting;

}
