package com.halo.core_bridge.api.jobposting.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.CareerType;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.TechStack;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PublicJobPostingDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicJobSearchRequest {
        private String keyword;
        private CareerType careerType;
        private List<TechStack> techStacks;

        private Integer page;
        private Integer size;

        public int getPage() {
            return page == null ? 0 : page;
        }

        public int getSize() {
            return size == null ? 12 : size;
        }
    }
    @Getter
    @AllArgsConstructor
    public static class JobRaw {
        private Long id;
        private String title;
        private String summary;
        private CareerType careerType;
        private String location;
        private LocalDateTime applyEndDate;
        private String departmentName;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Job {
        private Long id;
        private String title;
        private String summary;
        private String experience;
        private String location;
        private String deadline;
        private String department;
        private int views;

        public static Job fromRaw(JobRaw raw) {

            // 🔥 D-Day 계산
            long days = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    raw.getApplyEndDate().toLocalDate()
            );
            String dDay = (days < 0) ? "마감" : "D-" + days;

            // 🔥 경력 문자열 (label 그대로 사용)
            String exp = raw.getCareerType().getLabel();
            // (신입 / 경력 / 경력무관 그대로 표시됨)

            return Job.builder()
                    .id(raw.getId())
                    .title(raw.getTitle())
                    .summary(raw.getSummary())
                    .experience(exp)
                    .location(raw.getLocation())
                    .deadline(dDay)
                    .department(raw.getDepartmentName())
                    .views(1)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Jobs {
        private List<Job> jobs;
        private Long totalElements;
        private boolean last;

        public static Jobs from (List<JobRaw> raws, Long totalElements, boolean last) {
            return Jobs.builder()
                    .jobs(raws.stream().map(Job::fromRaw).toList())
                    .last(last)
                    .totalElements(totalElements)
                    .build();
        }
    }
}