package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class ResumeDto {

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Create {
        private String description;
        private Long jobPostingId;
        private List<CareerDto> careers;
        private List<CertificateDto> certificates;
        private List<EducationDto> educations;
        private List<LanguageDto> languages;
        private List<OverseasExperienceDto> overseasExperiences;
        private List<ResumeSkillDto> resumeSkills;
    }

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Update {
        private String description;
        private List<CareerDto> careers;
        private List<CertificateDto> certificates;
        private List<EducationDto> educations;
        private List<LanguageDto> languages;
        private List<OverseasExperienceDto> overseasExperiences;
        private List<ResumeSkillDto> resumeSkills;
    }

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Delete {
        // 필요한 경우 추가 필드
    }

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private LocalDateTime appliedAt;
        private String description;
        private Long jobPostingId;
        private Long userId;
        private List<CareerDto> careers;
        private List<CertificateDto> certificates;
        private List<EducationDto> educations;
        private List<LanguageDto> languages;
        private List<OverseasExperienceDto> overseasExperiences;
        private List<ResumeSkillDto> resumeSkills;

        public static Response from(Resume resume) {
            return Response.builder()
                    .id(resume.getId())
                    .appliedAt(resume.getApplied_at())
                    .description(resume.getDescription())
                    .jobPostingId(resume.getJobPosting() != null ? resume.getJobPosting().getId() : null)
                    .userId(resume.getUser() != null ? resume.getUser().getId() : null)
                    .careers(resume.getCareers().stream().map(CareerDto::from).toList())
                    .certificates(resume.getCertificates().stream().map(CertificateDto::from).toList())
                    .educations(resume.getEducations().stream().map(EducationDto::from).toList())
                    .languages(resume.getLanguages().stream().map(LanguageDto::from).toList())
                    .overseasExperiences(resume.getOverseasExperiences().stream().map(OverseasExperienceDto::from).toList())
                    .resumeSkills(resume.getResumeSkills().stream().map(ResumeSkillDto::from).toList())
                    .build();
        }
    }
}
