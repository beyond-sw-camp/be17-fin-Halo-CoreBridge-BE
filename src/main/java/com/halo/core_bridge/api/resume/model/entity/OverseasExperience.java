package com.halo.core_bridge.api.resume.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OverseasExperience extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    private Resume resume;
}
