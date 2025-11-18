package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Education;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class EducationDto {
    @Schema(description = "학력 ID", example = "1")
    private Long id;
    @Schema(description = "학교명", example = "한국대학교")
    private String schoolName;
    @Schema(description = "전공", example = "컴퓨터공학")
    private String major;
    @Schema(description = "학위", example = "학사")
    private String degree;

    public Education toEntity(Resume resume) {
        return Education.builder()
                .schoolName(this.schoolName)
                .major(this.major)
                .degree(this.degree)
                .resume(resume)
                .build();
    }

    public static EducationDto from(Education education) {
        return EducationDto.builder()
                .id(education.getId())
                .schoolName(education.getSchoolName())
                .major(education.getMajor())
                .degree(education.getDegree())
                .build();
    }
}
