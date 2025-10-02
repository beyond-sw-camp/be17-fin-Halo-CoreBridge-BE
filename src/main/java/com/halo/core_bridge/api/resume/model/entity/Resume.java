package com.halo.core_bridge.api.resume.model.entity;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.users.model.entity.User;
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
public class Resume extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime applied_at;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}