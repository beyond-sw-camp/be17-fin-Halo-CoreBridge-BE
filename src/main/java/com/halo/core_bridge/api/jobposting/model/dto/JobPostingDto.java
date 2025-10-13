package com.halo.core_bridge.api.jobposting.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.organization.model.entity.Department;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


public class JobPostingDto {



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {

        @NotBlank(message = "제목은 필수 입력값입니다.")
        @Size(max = 100, message = "제목은 100자 이하로 입력해주세요")
        private String title;

        @NotBlank(message = "채용공고 내용은 필수 입력값입니다.")
        @Size(max = 2000, message = "채용공고 내용은 2000자 이하로 입력해주세요.")
        private String description;

        @NotBlank(message = "고용형태는 필수 입력값입니다.")
        @Pattern(regexp= "^(정규직|계약직)$", message = "고용형태는 정규직 계약직 중 하나입니다.")
        private String employmentType;

        @NotBlank(message = "경력 구분은 필수 입력값입니다.")
        @Pattern(regexp = "^(신입|경력|무관)$", message = "경력 구분은 신입, 경력, 무관 중 하나여야 합니다.")
        private String careerType;

        private int minExperience;

        @AssertTrue(message = "최대 경력은 최소 경력 이상이어야 합니다.")
        private boolean isValidExperience() {
            return maxExperience >= minExperience;
        }

        private int maxExperience;

        @NotNull(message = "부서 ID는 필수 입력값입니다.")
        @Positive(message = "부서 ID는 양수여야 합니다.")
        private Long departmentId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "지원 시작일은 필수 입력값입니다.")
        private LocalDateTime applyStartDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "지원 마감일은 필수 입력값입니다.")
        private LocalDateTime applyEndDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "채용 마감일은 필수 입력값입니다.")
        private LocalDateTime hireEndDate;

        @NotEmpty(message = "기술 스택은 최소 1개 이상 입력해야 합니다.")
        private List<
                @NotBlank(message = "기술 이름은 비어 있을 수 없습니다.")
                @Size(max = 50, message = "기술 이름은 50자 이하로 입력해주세요.")
                        String
                > skills;

        public JobPosting toEntity(Department department) {
            return JobPosting.builder()
                    .title(title)
                    .description(description)
                    .employmentType(employmentType)
                    .careerType(careerType)
                    .minExperience(minExperience)
                    .maxExperience(maxExperience)
                    .applyStartDate(applyStartDate)
                    .applyEndDate(applyEndDate)
                    .hireEndDate(hireEndDate)
                    .department(department)
                    .build();
        }
    }

    // 공고 목록 응답
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private Long id;
        private String title;
        private String description;
        private String department;
        private String employmentType;
        private List<String> skills;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime applyStartDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime applyEndDate;

        public static DetailResponse fromEntity(JobPosting entity) {
            return DetailResponse.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .department(entity.getDepartment().getName())
                    .employmentType(entity.getEmploymentType())
                    .skills(
                            entity.getSkills() == null
                                    ? List.of()  // null일 경우 빈 리스트로 대체
                                    : entity.getSkills().stream()
                                    .map(JobPostingSkill::getName)
                                    .toList()
                    )
                    .applyStartDate(entity.getApplyStartDate())
                    .applyEndDate(entity.getApplyEndDate())
                    .build();
        }
    }
}
