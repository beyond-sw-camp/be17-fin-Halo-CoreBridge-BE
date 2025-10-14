package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.model.entity.ResumeSkill;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSkillDto {
    private Long id;
    private String name;

    public ResumeSkill toEntity(Resume resume) {
        return ResumeSkill.builder()
                .name(this.name)
                .resume(resume)
                .build();
    }

    public static ResumeSkillDto from(ResumeSkill resumeSkill) {
        return ResumeSkillDto.builder()
                .id(resumeSkill.getId())
                .name(resumeSkill.getName())
                .build();
    }
}
