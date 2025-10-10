package com.halo.core_bridge.api.jobposting.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.organization.model.entity.Department;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class JobPostingDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String title;
        private String description;
        private String employmentType;
        private String careerType;
        private int minExperience;
        private int maxExperience;
        private Long departmentId;
        private LocalDateTime applyStartDate;
        private LocalDateTime applyEndDate;
        private LocalDateTime hireEndDate;
        private List<String> skills;

        public JobPosting toEntity(Department department) {
            JobPosting posting = JobPosting.builder()
                    .title(this.title)
                    .description(this.description)
                    .employmentType(this.employmentType)
                    .careerType(this.careerType)
                    .minExperience(this.minExperience)
                    .maxExperience(this.maxExperience)
                    .applyStartDate(this.applyStartDate)
                    .applyEndDate(this.applyEndDate)
                    .hireEndDate(this.hireEndDate)
                    .department(department)
                    .build();

            if (skills != null) {
                this.skills.forEach(skillName ->
                        posting.addSkill(JobPostingSkill.builder().name(skillName).build()));
            }
            return posting;
        }

    }

    // 공고 목록 응답
    @Getter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ListResponse {
        private Long id;
        private String title;
        private String department;
        private String employmentType;
        private LocalDateTime applyEndDate;

        // entity → dto 변환
        public static ListResponse fromEntity(JobPosting entity) {
            return ListResponse.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .department(entity.getDepartment().getName())
                    .employmentType(entity.getEmploymentType())
                    .applyEndDate(entity.getApplyEndDate())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DetailResponse {
        private Long id;
        private String title;
        private String description;
        private String department;
        private String employmentType;
        private List<String> skills;
        private LocalDateTime applyStartDate;
        private LocalDateTime applyEndDate;

        public static DetailResponse fromEntity(JobPosting entity) {
            return DetailResponse.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .department(entity.getDepartment().getName())
                    .employmentType(entity.getEmploymentType())
                    .skills(entity.getSkills().stream()
                            .map(JobPostingSkill::getName)
                            .collect(Collectors.toList()))
                    .applyStartDate(entity.getApplyStartDate())
                    .applyEndDate(entity.getApplyEndDate())
                    .build();
        }
    }
}
