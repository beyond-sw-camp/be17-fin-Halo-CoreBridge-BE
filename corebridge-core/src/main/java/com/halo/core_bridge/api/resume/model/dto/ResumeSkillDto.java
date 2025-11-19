package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.model.entity.ResumeSkill;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSkillDto {
    @Schema(description = "이력서 기술 스택 ID", example = "1")
    private Long id;
    @Schema(description = "기술 스택 이름", example = "Java")
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
