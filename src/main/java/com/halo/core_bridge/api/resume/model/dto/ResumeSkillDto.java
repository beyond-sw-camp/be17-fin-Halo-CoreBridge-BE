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

    private Resume resume;

    public ResumeSkill toEntity() {
        return ResumeSkill.builder()
                .id(this.id)
                .name(this.name)
                .resume(this.resume)
                .build();
    }

    public static ResumeSkillDto from(ResumeSkill resumeSkill) {
        return ResumeSkillDto.builder()
                .id(resumeSkill.getId())
                .name(resumeSkill.getName())
                .resume(resumeSkill.getResume())
                .build();
    }
}