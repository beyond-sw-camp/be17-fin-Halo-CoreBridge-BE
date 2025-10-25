package com.halo.core_bridge.api.jobposting.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.jobposting.model.entity.*;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class JobPostingDto {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        //기본 정보
        @NotBlank(message = "제목은 필수 입력값입니다.")
        @Size(max = 100, message = "제목은 100자 이하로 입력해주세요")
        private String title;

        @NotNull(message = "고용 형태는 필수 입력값입니다.")
        private EmploymentType employmentType; // Enum(정규직,계약직,인턴)

        @NotNull(message = "경력 선택은 필수 입력값입니다.")
        private CareerType careerType; // Enum(신입, 경력, 무관)

        @Min(value = 1, message = "최소 경력은 1년 이상이어야 합니다")
        private Integer minExperience;
        @Min(value = 1, message = "최대 경력은 1년 이상이어야 합니다")
        private Integer maxExperience;

        @AssertTrue(message = "최대 경력은 최소경력 이상이어야 합니다.")
        private boolean isValidExperience() {
            if (minExperience == null || maxExperience == null) {
                return true;
            }
            return maxExperience >= minExperience;
        }

        @Size(max = 10, message = "직급은 10자 이하로 입력해주세요")
        private String positionLevel;

        @NotBlank(message = "근무지역은 필수 입력값입니다.")
        @Size(max = 100, message = "근무지역은 100자 이하로 입력해주세요")
        private String location;

        // 공고 기간(날짜)관련 정보
        @NotNull(message = "접수 시작일은 필수 입력값입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime applyStartDate;

        @NotNull(message = "접수 종료일은 필수 입력값입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime applyEndDate;

        @NotNull(message = "마감일은 필수 입력값입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime hireEndDate;

        // 모집
        @NotNull(message = "모집 인원은 필수 입력값입니다.")
        @Min(value = 0, message = "모집인원은 0명 이상이어야 합니다.")
        private Integer headcount;

        // 직무 상세

        @NotBlank(message = "직무 소개는 필수 입력값입니다.")
        @Size(max = 1000, message = "직무 소개는 1000자 이하로 입력해주세요")
        private String summary;

        @NotBlank(message = "주요 업무는 필수 입력값입니다.")
        @Size(max = 1000, message = "직무 소개는 1000자 이하로 입력해주세요")
        private String responsibilities;

        @NotBlank(message = "필수 자격 요건은 필수 입력값입니다.")
        @Size(max = 1000, message = "직무 소개는 1000자 이하로 입력해주세요")
        private String requirements;

        @NotBlank(message = "우대 사항은 필수 입력값입니다.")
        @Size(max = 1000, message = "우대 사항은 1000자 이하로 입력해주세요.")
        private String preferred;

        @NotEmpty(message = "기술 스택은 최소 1개 이상 입력해야 합니다.")
        private List<
                @Size(max = 20, message = "기술명은 20자 이하로 입력해주세요.")
                        String> techStack; // ex) ["Java", "Spring", "Vue"]


        @NotEmpty(message = "채용 프로세스는 최소 1개 이상 입력해야 합니다.")
        private List<
                @NotBlank(message = "프로세스명은 비어 있을 수 없습니다.")
                @Size(max = 50, message = "프로세스명은 50자 이하로 입력해주세요.")
                        String> recruitProcess;             // ["지원 완료","서류 검토","1차 면접",...]

        // 급여
        @NotNull(message = "급여 형태는 필수 입력값입니다.")
        private SalaryType salaryType;

        @PositiveOrZero(message = "최소 금액은 0 이상이어야 합니다.")
        private Integer salaryMin;

        @PositiveOrZero(message = "최대 금액은 0 이상이어야 합니다.")
        private Integer salaryMax;

        private Boolean salaryNegotiable;

        // 근무 조건
        @NotBlank(message = "근무시간은 필수 입력값입니다.")
        private String workingHours;         // 예: "09:00 ~ 18:00 (주 5일)"

        @NotBlank(message = "복리후생은 필수 입력값입니다.")
        private String benefits;

        // 부서/담당/기타
        @NotNull(message = "부서 ID는 필수 입력값입니다.")
        @Positive(message = "부서 ID는 양수여야 합니다.")
        private Long departmentId;

        @NotBlank(message = "담당자 이름은 필수 입력값입니다.")
        private String contactName;

        @NotBlank(message = "담당자 이메일은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String contactEmail;

        private String additionalInfo;

        public JobPosting toEntity(Long createdByUserId) {
            return JobPosting.builder()
                    .title(title)
                    .createdUser(User.builder().id(createdByUserId).build())
                    .employmentType(employmentType)
                    .careerType(careerType)
                    .positionLevel(positionLevel)
                    .location(location)
                    .applyStartDate(applyStartDate)
                    .applyEndDate(applyEndDate)
                    .hireEndDate(hireEndDate)
                    .headcount(headcount)
                    .minExperience(minExperience == null ? 0 : minExperience)
                    .maxExperience(maxExperience == null ? 0 : maxExperience)
                    .summary(summary)
                    .responsibilities(responsibilities)
                    .requirements(requirements)
                    .preferred(preferred)
                    .salaryType(salaryType)
                    .salaryMin(salaryMin)
                    .salaryMax(salaryMax)
                    .salaryNegotiable(salaryNegotiable != null ? salaryNegotiable : Boolean.FALSE)
                    .workingHours(workingHours)
                    .benefits(benefits)
                    .contactName(contactName)
                    .contactEmail(contactEmail)
                    .additionalInfo(additionalInfo)
                    .department(Department.builder().id(departmentId).build())
                    .build();
        }


    }

    // 공고 목록 응답
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobPostingListResponseDto {
        // 기본 공고 정보
        private Long id; // 공고 Id
        private String title; // 공고명(ex: 시니어 프론트엔드 개발자)

        // 요약정보(ex: "5년 이상 · 정규직")
        private String summaryText;

        private String departmentName; // 부서명(ex: 개발팀)
        private EmploymentType employmentType;
        private CareerType careerType;

        // 상태/날짜 관련
        private String status; // ex: "채용중" / "예정" / "마감"

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hireEndDate;       // 마감일 (YYYY-MM-DD)

        private String dday;                // 예: D-16

        // 지원자/진행률
        private Integer applicantCount; //총 지원자 수
        private Integer progressPercent; //진행률(0 ~ 100)

        // 단계별 현황
        private List<ProcessSummary> processSummaries;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ProcessSummary {
            private String stageName;   // 단계명 (예: 서류, 1차, 2차, 최종)
            private Integer count;      // 해당 단계 지원자 수
            private Integer orderIndex; // 단계 순서 (예: 0=서류, 1=1차 ...)
        }

        // 정적 팩토리 메서드 - 엔티티에서 바로 DTO 변환
        public static JobPostingListResponseDto fromEntity(
                JobPosting entity,
                Integer applicantCount,
                List<ProcessSummary> proccessSummaries
        ) {
            JobPostingListResponseDto dto = JobPostingListResponseDto.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .departmentName(entity.getDepartment().getName())
                    .employmentType(entity.getEmploymentType())
                    .careerType(entity.getCareerType())
                    .hireEndDate(entity.getHireEndDate().toLocalDate())
                    .applicantCount(applicantCount)
                    .processSummaries(proccessSummaries)
                    .build();

            dto.summaryText = dto.buildSummaryText();
            dto.status = dto.computeStatus(entity.getApplyStartDate(), entity.getHireEndDate());
            dto.dday = dto.computeDDay(entity.getHireEndDate());
            dto.progressPercent = dto.computeProgressByPeriod(entity.getApplyStartDate(), entity.getHireEndDate());

            return dto;
        }
        // dto 내부 로직 - 데이터 가공/계산

        //요약 정보 생성("5년 이상 · 정규직")
        private String buildSummaryText() {
            String exp = careerType.getLabel();
            String emp =employmentType.getLabel();
            return exp + " · " + emp;
        }

        // 채용 상태 계산("예정" / "채용중" / "마감")
        private String computeStatus(LocalDateTime start, LocalDateTime end) {
            LocalDateTime now =  LocalDateTime.now();
            if(now.isBefore(start)) return "예정";
            if(now.isAfter(end)) return "마감";
            return "채용중";
        }

        private String computeDDay(LocalDateTime end) {
            long diff = ChronoUnit.DAYS.between(LocalDate.now(), end.toLocalDate());

            if(diff > 0) {
                return "D-" + diff;
            } else if (diff == 0) {
                return "D-Day";
            } else {
                return "마감";
            }
        }

        private Integer computeProgressByPeriod(LocalDateTime startDate, LocalDateTime endDate) {

            LocalDate today = LocalDate.now();
            LocalDate start = startDate.toLocalDate();
            LocalDate end = endDate.toLocalDate();

            long totalDays =  ChronoUnit.DAYS.between(start, end);

            long passedDays = ChronoUnit.DAYS.between(start, today);
            double progress = (double) passedDays / totalDays * 100;

            if (progress < 0) return 0;
            if (progress > 100) return 100;

            return (int) Math.round(progress);
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
            return null;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String title;
        private String description;
        private String employmentType;
        private String careerType;
        private Integer minExperience;
        private Integer maxExperience;
        private Long departmentId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime applyStartDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime applyEndDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime hireEndDate;
        private List<String> skills;
    }


}
