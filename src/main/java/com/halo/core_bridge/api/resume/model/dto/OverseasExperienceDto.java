package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.OverseasExperience;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverseasExperienceDto {
    private Long id;
    private String type;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;

    public OverseasExperience toEntity(Resume resume) {
        return OverseasExperience.builder()
                .type(this.type)
                .country(this.country)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .note(this.note)
                .resume(resume)
                .build();
    }

    public static OverseasExperienceDto from(OverseasExperience overseasExperience) {
        return OverseasExperienceDto.builder()
                .id(overseasExperience.getId())
                .type(overseasExperience.getType())
                .country(overseasExperience.getCountry())
                .startDate(overseasExperience.getStartDate())
                .endDate(overseasExperience.getEndDate())
                .note(overseasExperience.getNote())
                .build();
    }
}
