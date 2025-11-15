package com.halo.core_bridge.api.jobposting.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.coverLetterTitle.model.dto.CoverLetterTitleDto;
import com.halo.core_bridge.api.jobposting.model.document.JobPostingDocument;
import com.halo.core_bridge.api.jobposting.model.entity.*;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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

        @Size(min = 1, message = "기술 스택은 최소 1개 이상 선택해주세요.")
        private List<String> techStack;


        @NotEmpty(message = "채용 프로세스는 최소 1개 이상 입력해야 합니다.")
        @Valid
        private List<RecruitProcessDto.Create> recruitProcess;

        @NotNull(message = "질문 항목 리스트는 비워둘 수 없습니다.")
        @Size(min = 3, message = "질문 항목은 최소 3개 이상 입력해야 합니다.")
        @Valid
        private List<CoverLetterTitleDto.CoverLetterTitleRequest> coverLetterTitles;

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
        @NotNull(message = "부서는 필수 입력값입니다.")
        @Positive(message = "부서 ID는 양수여야 합니다.")
        private Long departmentId;

        @NotBlank(message = "담당자 이름은 필수 입력값입니다.")
        private String contactName;

        @NotBlank(message = "담당자 이메일은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String contactEmail;

        private String additionalInfo;

        @NotNull(message = "면접관을 필수로 등록해야합니다.")
        @Size(min = 1, message = "한명 이상의 면접관을 선택해주세요.")
        private List<Long> interviewers;

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
            String emp = employmentType.getLabel();
            return exp + " · " + emp;
        }

        // 채용 상태 계산("예정" / "채용중" / "마감")
        private String computeStatus(LocalDateTime start, LocalDateTime end) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(start)) return "예정";
            if (now.isAfter(end)) return "마감";
            return "채용중";
        }

        private String computeDDay(LocalDateTime end) {
            long diff = ChronoUnit.DAYS.between(LocalDate.now(), end.toLocalDate());

            if (diff > 0) {
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

            long totalDays = ChronoUnit.DAYS.between(start, end);

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
    public static class HeaderResponse {
        private Long id;
        //기본 정보
        private String title;
        private String status;
        private String departmentName;
        private EmploymentType employmentType;
        private String location;
        private CareerType careerType;
        private Integer minExperience;
        private Integer maxExperience;
        private List<TechStack> skills;
        private SalaryType salaryType;
        private Integer salaryMin;
        private Integer salaryMax;
        private Boolean SalaryNegotiable;

        public static HeaderResponse fromEntity(JobPosting entity) {
            List<TechStack> skills = entity.getSkills().stream().map(JobPostingSkill::getName).toList();
            String status = HeaderResponse.computeStatus(entity.getApplyStartDate(), entity.getHireEndDate());
            return HeaderResponse.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .status(status)
                    .departmentName(entity.getDepartment().getName())
                    .employmentType(entity.getEmploymentType())
                    .location(entity.getLocation())
                    .careerType(entity.getCareerType())
                    .minExperience(entity.getMinExperience() == null ? 0 : entity.getMinExperience())
                    .maxExperience(entity.getMaxExperience() == null ? 0 : entity.getMaxExperience())
                    .skills(skills)
                    .salaryType(entity.getSalaryType())
                    .salaryMin(entity.getSalaryMin())
                    .salaryMax(entity.getSalaryMax())
                    .SalaryNegotiable(entity.getSalaryNegotiable())
                    .build();
        }

        private static String computeStatus(LocalDateTime start, LocalDateTime end) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(start)) return "예정";
            if (now.isAfter(end)) return "마감";
            return "채용중";
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        //기본 식별자
        private Long id;

        //기본 회사소개
        private String summary; //직무 소개
        private String responsibilities; // 주요 업무
        private String requirements; // 필수 자격 요건
        private String preferred; // 우대 사항
        private String benefits; // 복리후생
        private String additionalInfo; //기타 안내사항

        //공고 정보
        private String status; // ex)채용중 , 마감, 예정

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDateTime createDate; //등록일
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        private LocalDateTime applyStartDate; //접수시작일
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        private LocalDateTime applyEndDate; //접수마감일
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        private LocalDateTime hireEndDate; //채용마감일

        private Integer headCount; //모집 인원
        private Integer applicantCount; //지원자 수

        private List<TechStack> skills;
        private List<RecruitProcessDto.Read> recruitProcesses;

        private String workingHours;
        private String location;

        private String contactName;
        private String contactEmail;
    }

    @Getter
    @Builder
    public static class EditResponse {
        private Long id;
        private String title;

        private String employmentType; // Enum → String
        private String careerType;     // Enum → String

        private Integer minExperience;
        private Integer maxExperience;
        private String positionLevel;
        private String location;

        // 날짜들은 문자열 형태로 내려감 (TypeScript: string)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime applyStartDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime applyEndDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime hireEndDate;

        private Integer headcount;

        private String summary;
        private String responsibilities;
        private String requirements;
        private String preferred;

        private List<TechStack> techStack; // ex) ["Java", "Spring Boot"]

        private List<RecruitProcessDto.Read> recruitProcess;
        private List<CoverLetterTitleDto.CoverLetterTitleResponse> coverLetterTitles;

        private String salaryType; // Enum → String
        private Integer salaryMin;
        private Integer salaryMax;
        private Boolean salaryNegotiable;

        private String workingHours;
        private String benefits;

        private Long departmentId;
        private String contactName;
        private String contactEmail;
        private String additionalInfo;

        public static EditResponse fromEntity(JobPosting jobPosting, List<CoverLetterTitleDto.CoverLetterTitleResponse> coverLetterTitles) {
            return EditResponse.builder()
                    .id(jobPosting.getId())
                    .title(jobPosting.getTitle())
                    .employmentType(jobPosting.getEmploymentType().getLabel())
                    .careerType(jobPosting.getCareerType().getLabel())
                    .minExperience(jobPosting.getMinExperience())
                    .maxExperience(jobPosting.getMaxExperience())
                    .positionLevel(jobPosting.getPositionLevel())
                    .location(jobPosting.getLocation())
                    .applyStartDate(jobPosting.getApplyStartDate())
                    .applyEndDate(jobPosting.getApplyEndDate())
                    .hireEndDate(jobPosting.getHireEndDate())
                    .headcount(jobPosting.getHeadcount())
                    .summary(jobPosting.getSummary())
                    .responsibilities(jobPosting.getResponsibilities())
                    .requirements(jobPosting.getRequirements())
                    .preferred(jobPosting.getPreferred())
                    .techStack(
                            jobPosting.getSkills().stream()
                                    .map(JobPostingSkill::getName)
                                    .toList()
                    )
                    .recruitProcess(
                            jobPosting.getRecruitProcesses().stream()
                                    .sorted(Comparator.comparing(RecruitProcess::getOrderIdx))
                                    .map(RecruitProcessDto.Read::from)
                                    .toList()
                    )
                    .salaryType(jobPosting.getSalaryType().getLabel())
                    .coverLetterTitles(coverLetterTitles)
                    .salaryMin(jobPosting.getSalaryMin())
                    .salaryMax(jobPosting.getSalaryMax())
                    .salaryNegotiable(jobPosting.getSalaryNegotiable())
                    .workingHours(jobPosting.getWorkingHours())
                    .benefits(jobPosting.getBenefits())
                    .departmentId(jobPosting.getDepartment().getId())
                    .contactName(jobPosting.getContactName())
                    .contactEmail(jobPosting.getContactEmail())
                    .additionalInfo(jobPosting.getAdditionalInfo())
                    .build();
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        // 기본 정보
        // ----------------------------
        // 기본 정보
        // ----------------------------
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
        @Valid
        private List<RecruitProcessDto.Create> recruitProcess;

        @NotNull(message = "질문 항목 리스트는 비워둘 수 없습니다.")
        @Size(min = 3, message = "질문 항목은 최소 3개 이상 입력해야 합니다.")
        @Valid
        private List<CoverLetterTitleDto.CoverLetterTitleRequest> coverLetterTitles;

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
        @NotNull(message = "부서는 필수 입력값입니다.")
        @Positive(message = "부서 ID는 양수여야 합니다.")
        private Long departmentId;

        @NotBlank(message = "담당자 이름은 필수 입력값입니다.")
        private String contactName;

        @NotBlank(message = "담당자 이메일은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String contactEmail;

        private String additionalInfo;

        public void applyUpdates(JobPosting jobPosting) {
            // ------------------------------
            // 기본 정보
            // ------------------------------
            if (title != null) jobPosting.setTitle(title);
            if (employmentType != null) jobPosting.setEmploymentType(employmentType);
            if (careerType != null) jobPosting.setCareerType(careerType);
            if (minExperience != null) jobPosting.setMinExperience(minExperience);
            if (maxExperience != null) jobPosting.setMaxExperience(maxExperience);
            if (positionLevel != null) jobPosting.setPositionLevel(positionLevel);
            if (location != null) jobPosting.setLocation(location);

            // ------------------------------
            // 기간 관련
            // ------------------------------
            if (applyStartDate != null) jobPosting.setApplyStartDate(applyStartDate);
            if (applyEndDate != null) jobPosting.setApplyEndDate(applyEndDate);
            if (hireEndDate != null) jobPosting.setHireEndDate(hireEndDate);

            // ------------------------------
            // 모집 정보
            // ------------------------------
            if (headcount != null) jobPosting.setHeadcount(headcount);
            if (summary != null) jobPosting.setSummary(summary);
            if (responsibilities != null) jobPosting.setResponsibilities(responsibilities);
            if (requirements != null) jobPosting.setRequirements(requirements);
            if (preferred != null) jobPosting.setPreferred(preferred);

            // ------------------------------
            // 급여 관련
            // ------------------------------
            if (salaryType != null) jobPosting.setSalaryType(salaryType);
            if (salaryMin != null) jobPosting.setSalaryMin(salaryMin);
            if (salaryMax != null) jobPosting.setSalaryMax(salaryMax);
            if (salaryNegotiable != null) jobPosting.setSalaryNegotiable(salaryNegotiable);

            // ------------------------------
            // 근무 조건
            // ------------------------------
            if (workingHours != null) jobPosting.setWorkingHours(workingHours);
            if (benefits != null) jobPosting.setBenefits(benefits);

            // ------------------------------
            // 부서/담당/기타
            // ------------------------------
            if (departmentId != null) {
                jobPosting.setDepartment(Department.builder().id(departmentId).build());
            }
            if (contactName != null) jobPosting.setContactName(contactName);
            if (contactEmail != null) jobPosting.setContactEmail(contactEmail);
            if (additionalInfo != null) jobPosting.setAdditionalInfo(additionalInfo);
        }

    }

    @Getter
    @Builder
    public static class SearchQuery {
        private String keyword;
        private int page;

        public static SearchQuery from(String keyword, int page) {
            return SearchQuery.builder()
                    .keyword(keyword)
                    .page(page)
                    .build();
        }
    }


    @Getter
    @Builder
    public static class JobPostingListDto {

        List<JobPostingListResponseDto> jobPostings;
        private int currentPage;
        private int totalPages;
        private long totalElements;

        public static JobPostingListDto from(List<JobPostingListResponseDto> jobPostings, int currentPage, int totalPages, long totalElements) {
            return JobPostingListDto.builder()
                    .jobPostings(jobPostings)
                    .currentPage(currentPage)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JobPostingQuery {

        private Long id;
        private String title;
        private String departmentName;
        private EmploymentType employmentType;
        private CareerType careerType;
        private LocalDateTime hireEndDate;
        private LocalDateTime applyStartDate;
        private Long applicantCount;
        List<RecruitProcessDto.ProcessSummary> processSummaries;

        public static JobPostingQuery from(Long id,
                                           String title,
                                           String departmentName,
                                           EmploymentType employmentType,
                                           CareerType careerType,
                                           LocalDateTime hireEndDate,
                                           LocalDateTime applyStartDate,
                                           Long applicantCount,
                                           List<RecruitProcessDto.ProcessSummary> processSummaries) {

            return JobPostingQuery.builder()
                    .id(id)
                    .title(title)
                    .departmentName(departmentName)
                    .employmentType(employmentType)
                    .careerType(careerType)
                    .hireEndDate(hireEndDate)
                    .applyStartDate(applyStartDate)
                    .applicantCount(applicantCount)
                    .processSummaries(processSummaries)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobPostingsResp {
        private Long id;
        private String title;
        private String summaryText;
        private String departmentName;
        private EmploymentType employmentType;
        private CareerType careerType;
        private String status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hireEndDate;
        private String dday;
        private Long applicantCount;
        private Integer progressPercent;
        private List<RecruitProcessDto.ProcessSummary> processSummaries;

        public static JobPostingsResp from(JobPostingQuery query, String status, String dDay, Integer progressPercent) {

            return JobPostingsResp.builder()
                    .id(query.getId())
                    .title(query.getTitle())
                    .departmentName(query.getDepartmentName())
                    .employmentType(query.getEmploymentType())
                    .careerType(query.getCareerType())
                    .hireEndDate(query.getHireEndDate().toLocalDate())
                    .applicantCount(query.getApplicantCount())
                    .processSummaries(query.getProcessSummaries())
                    .dday(dDay)
                    .status(status)
                    .summaryText(buildSummaryText(query.getCareerType(), query.getEmploymentType()))
                    .progressPercent(progressPercent)
                    .build();
        }

        public static JobPostingsResp fromJobPostingDocument(JobPostingDocument query, String status, String dDay, Integer progressPercent) {

            return JobPostingsResp.builder()
                    .id(query.getId())
                    .title(query.getTitle())
                    .departmentName(query.getDepartmentName())
                    .employmentType(query.getEmploymentType())
                    .careerType(query.getCareerType())
                    .hireEndDate(query.getApplyEndDate().toLocalDate())
                    .applicantCount(query.getApplicantCount())
                    .processSummaries(query.getProcesses().stream().map(RecruitProcessDto.ProcessSummary::from).toList())
                    .dday(dDay)
                    .status(status)
                    .summaryText(buildSummaryText(query.getCareerType(), query.getEmploymentType()))
                    .progressPercent(progressPercent)
                    .build();
        }

        private static String buildSummaryText(CareerType careerType, EmploymentType employmentType) {
            String exp = careerType.getLabel();
            String emp = employmentType.getLabel();
            return exp + " · " + emp;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobPostingPage {
        private List<JobPostingsResp> jobPostings;
        private int currentPage;
        private int totalPages;
        private long totalElements;

        public static JobPostingPage from(List<JobPostingsResp> jobPostings, int currentPage, int totalPages, long totalElements) {

            return JobPostingPage.builder()
                    .jobPostings(jobPostings)
                    .currentPage(currentPage)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();

        }

        public static JobPostingPage fromJobPostingDocument(List<JobPostingDocument> jobPostingDocuments, int currentPage, int totalPages, long totalElements) {

            return JobPostingPage.builder()
                    .jobPostings(jobPostingDocuments.stream().map(
                            document -> JobPostingsResp.fromJobPostingDocument(document, document.getStatus(), document.getDDay(), document.getProgressRate())
                    ).toList())
                    .currentPage(currentPage)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        }
    }
}



