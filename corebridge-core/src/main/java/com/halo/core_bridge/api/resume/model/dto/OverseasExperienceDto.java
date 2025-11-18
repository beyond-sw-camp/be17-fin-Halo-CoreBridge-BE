package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.OverseasExperience;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverseasExperienceDto {
    @Schema(description = "해외 경험 ID", example = "1")
    private Long id;
    @Schema(description = "유형", example = "교환학생")
    private String type;
    @Schema(description = "국가", example = "미국")
    private String country;
    @Schema(description = "시작일", example = "2018-09-01")
    private LocalDate startDate;
    @Schema(description = "종료일", example = "2019-02-28")
    private LocalDate endDate;
    @Schema(description = "비고", example = "미국 캘리포니아 주립대학교 교환학생")
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
