package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Education;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {

    private Long id;
    private String schoolName;
    private String major;
    private String degree;

    private Resume resume;

    public Education toEntity() {
        return Education.builder()
                .id(this.id)
                .schoolName(this.schoolName)
                .major(this.major)
                .degree(this.degree)
                .resume(this.resume)
                .build();
    }

    public static EducationDto from(Education education) {
        return EducationDto.builder()
                .id(education.getId())
                .schoolName(education.getSchoolName())
                .major(education.getMajor())
                .degree(education.getDegree())
                .resume(education.getResume())
                .build();
    }
}