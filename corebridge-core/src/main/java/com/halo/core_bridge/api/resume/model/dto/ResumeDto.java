package com.halo.core_bridge.api.resume.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.coverLetterDescription.model.dto.CoverLetterDescriptionDto;
import com.halo.core_bridge.api.jobposting.model.entity.CareerType;
import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ResumeDto {

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Create {
        @Schema(description = "이력서 설명", example = "저는 백엔드 개발자로서 5년간의 경력을 가지고 있습니다.")
        private String description;
        @Schema(description = "채용 공고 ID", example = "1")
        private Long jobPostingId;
        @Schema(description = "경력 목록")
        private List<CareerDto> careers;
        @Schema(description = "자격증 목록")
        private List<CertificateDto> certificates;
        @Schema(description = "학력 목록")
        private List<EducationDto> educations;
        @Schema(description = "어학 목록")
        private List<LanguageDto> languages;
        @Schema(description = "해외 경험 목록")
        private List<OverseasExperienceDto> overseasExperiences;
        @Schema(description = "이력서 기술 스택 목록")
        private List<ResumeSkillDto> resumeSkills;

        private List<CoverLetterDescriptionDto.CoverLetterDescriptionRequest> descriptions;
    }

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Update {
        @Schema(description = "이력서 설명", example = "업데이트된 이력서 설명입니다.")
        private String description;
        @Schema(description = "경력 목록")
        private List<CareerDto> careers;
        @Schema(description = "자격증 목록")
        private List<CertificateDto> certificates;
        @Schema(description = "학력 목록")
        private List<EducationDto> educations;
        @Schema(description = "어학 목록")
        private List<LanguageDto> languages;
        @Schema(description = "해외 경험 목록")
        private List<OverseasExperienceDto> overseasExperiences;
        @Schema(description = "이력서 기술 스택 목록")
        private List<ResumeSkillDto> resumeSkills;
    }

//    @Getter @Setter @Builder
//    @NoArgsConstructor @AllArgsConstructor
//    public static class Delete {
//        // 필요한 경우 추가 필드
//    }

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        @Schema(description = "이력서 ID", example = "1")
        private Long id;
        @Schema(description = "지원일", example = "2025-10-14T10:00:00")
        private LocalDateTime appliedAt;
        @Schema(description = "이력서 설명", example = "저는 백엔드 개발자로서 5년간의 경력을 가지고 있습니다.")
        private String description;
        @Schema(description = "채용 공고 ID", example = "1")
        private Long jobPostingId;
        @Schema(description = "사용자 ID", example = "10")
        private Long userId;
        private String name;
        private String email;
        private String phone;
        private PdfDto.PdfResponseDto pdf;
        @Schema(description = "경력 목록")
        private List<CareerDto> careers;
        @Schema(description = "자격증 목록")
        private List<CertificateDto> certificates;
        @Schema(description = "학력 목록")
        private List<EducationDto> educations;
        @Schema(description = "어학 목록")
        private List<LanguageDto> languages;
        @Schema(description = "해외 경험 목록")
        private List<OverseasExperienceDto> overseasExperiences;
        @Schema(description = "이력서 기술 스택 목록")
        private List<ResumeSkillDto> resumeSkills;

        public static Response from(Resume resume) {
            return Response.builder()
                    .id(resume.getId())
                    .appliedAt(resume.getApplied_at())
                    .description(resume.getDescription())
                    .jobPostingId(resume.getJobPosting() != null ? resume.getJobPosting().getId() : null)
                    .userId(resume.getUser() != null ? resume.getUser().getId() : null)
                    .careers(resume.getCareers().stream()
                            .map(CareerDto::from)
                            .toList())
                    .certificates(resume.getCertificates().stream()
                            .map(CertificateDto::from)
                            .toList())
                    .educations(resume.getEducations().stream()
                            .map(EducationDto::from)
                            .toList())
                    .languages(resume.getLanguages().stream()
                            .map(LanguageDto::from)
                            .toList())
                    .overseasExperiences(resume.getOverseasExperiences().stream()
                            .map(OverseasExperienceDto::from)
                            .toList())
                    .resumeSkills(resume.getResumeSkills().stream()
                            .map(ResumeSkillDto::from)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ApplicantResponse {
        private Long id;
        private String name;
        private String email;
        private CareerType careerType;
        private List<String> skills;
        private String degree;
        private int certificateCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime applyDate;
        private String stageName;
    }
}
