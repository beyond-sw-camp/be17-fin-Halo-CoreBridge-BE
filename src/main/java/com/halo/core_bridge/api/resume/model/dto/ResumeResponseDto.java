package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class ResumeResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private Long id;
        private String description;
        private List<CareerDto> careers;
        private List<EducationDto> educations;
        private List<CertificateDto> certificates;
        private List<LanguageDto> languages;
        private List<OverseasExperienceDto> overseasExperiences;
        private List<ResumeSkillDto> resumeSkills;

        public static Detail from(Resume resume) {
            return Detail.builder()
                    .id(resume.getId())
                    .description(resume.getDescription())
                    .careers(resume.getCareers().stream().map(CareerDto::from).collect(Collectors.toList()))
                    .educations(resume.getEducations().stream().map(EducationDto::from).collect(Collectors.toList()))
                    .certificates(resume.getCertificates().stream().map(CertificateDto::from).collect(Collectors.toList()))
                    .languages(resume.getLanguages().stream().map(LanguageDto::from).collect(Collectors.toList()))
                    .overseasExperiences(resume.getOverseasExperiences().stream().map(OverseasExperienceDto::from).collect(Collectors.toList()))
                    .resumeSkills(resume.getResumeSkills().stream().map(ResumeSkillDto::from).collect(Collectors.toList()))
                    .build();
        }
    }
}