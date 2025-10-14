package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Education;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class EducationDto {
    private Long id;
    private String schoolName;
    private String major;
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
