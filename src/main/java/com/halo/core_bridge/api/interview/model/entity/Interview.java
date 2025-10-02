package com.halo.core_bridge.api.interview.model.entity;

import com.halo.core_bridge.api.resume.model.entity.Resume;
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
public class Interview extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Resume resume;
}
