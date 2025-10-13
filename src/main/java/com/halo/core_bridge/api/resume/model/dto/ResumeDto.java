package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.resume.model.entity.*;
import com.halo.core_bridge.api.users.model.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {

    private Long id;
    private String description;

    private JobPosting jobPosting;
    private User user;

    public Resume toEntity() {
        return Resume.builder()
                .id(this.id)
                .description(this.description)
                .jobPosting(this.jobPosting)
                .user(this.user)
                .build();
    }

    public static ResumeDto from(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .description(resume.getDescription())
                .jobPosting(resume.getJobPosting())
                .user(resume.getUser())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String description;
        private List<CareerDto> careers;
        private List<EducationDto> educations;
        private List<CertificateDto> certificates;
        private List<LanguageDto> languages;
        private List<OverseasExperienceDto> overseasExperiences;
        private List<ResumeSkillDto> resumeSkills;

        public Resume toEntity(User user, JobPosting jobPosting) {
            Resume resume = Resume.builder()
                    .description(this.description)
                    .user(user)
                    .jobPosting(jobPosting)
                    .build();

            this.careers.stream().map(CareerDto::toEntity).forEach(resume::addCareer);
            this.educations.stream().map(EducationDto::toEntity).forEach(resume::addEducation);
            this.certificates.stream().map(CertificateDto::toEntity).forEach(resume::addCertificate);
            this.languages.stream().map(LanguageDto::toEntity).forEach(resume::addLanguage);
            this.overseasExperiences.stream().map(OverseasExperienceDto::toEntity).forEach(resume::addOverseasExperience);
            this.resumeSkills.stream().map(ResumeSkillDto::toEntity).forEach(resume::addResumeSkill);

            return resume;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String description;
        private List<CareerDto> careers;
        private List<EducationDto> educations;
        private List<CertificateDto> certificates;
        private List<LanguageDto> languages;
        private List<OverseasExperienceDto> overseasExperiences;
        private List<ResumeSkillDto> resumeSkills;
    }
}
