package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Career;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CareerDto {
    private Long id;
    private String companyName;
    private String position;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Career toEntity(Resume resume) {
        return Career.builder()
                .companyName(this.companyName)
                .position(this.position)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .resume(resume)
                .build();
    }

    public static CareerDto from(Career career) {
        return CareerDto.builder()
                .id(career.getId())
                .companyName(career.getCompanyName())
                .position(career.getPosition())
                .startDate(career.getStartDate())
                .endDate(career.getEndDate())
                .build();
    }
}
