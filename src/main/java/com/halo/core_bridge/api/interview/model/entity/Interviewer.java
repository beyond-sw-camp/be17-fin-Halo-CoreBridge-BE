package com.halo.core_bridge.api.interview.model.entity;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@BatchSize(size = 100)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Interviewer extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
