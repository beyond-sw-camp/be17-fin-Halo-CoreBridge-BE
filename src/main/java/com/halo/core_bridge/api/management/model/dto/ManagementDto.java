package com.halo.core_bridge.api.management.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.users.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Builder
public class ManagementDto {
    private Long jobPostingId;
    private List<StageDto> stages;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageDto {
        private Long id;
        private String name;
        private String code;
        private List<ApplicantDto> applicants;

        public static StageDto from(RecruitProcess process, List<Resume> resumes) {
            return StageDto.builder()
                    .id(process.getId())
                    .name(process.getName())
                    .code(process.getCode())
                    .applicants(resumes.stream().map(ApplicantDto::from).toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicantDto {
        private Long id;
        private String name;
        private double experience;
        private long daysSinceApplied;

        public static ApplicantDto from(Resume resume) {

            User user = resume.getUser();

            //접수일 계산
            long days = ChronoUnit.DAYS.between(resume.getApplied_at(), LocalDateTime.now());

            // 경력계산
            double totalYears = resume.getCareers().stream()
                    .mapToDouble(c -> ChronoUnit.DAYS.between(
                            c.getStartDate().toLocalDate(),
                            c.getEndDate().toLocalDate()) / 365.0)
                    .sum();


            return ApplicantDto.builder()
                    .id(resume.getId())
                    .name(user.getName())
                    .experience(Math.round(totalYears * 10) / 10.0)
                    .daysSinceApplied(days)
                    .build();
        }

    }

    public static ManagementDto of(Long jobPostingId, List<StageDto> stages) {
        return ManagementDto.builder()
                .jobPostingId(jobPostingId)
                .stages(stages)
                .build();
    }
}
