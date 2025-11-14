package com.halo.core_bridge.api.jobposting.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.CareerType;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.TechStack;
import lombok.*;

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
    @Builder
    public static class Job {
        private Long id;
        private String title;
        private String summary;
        private String experience;
        private String location;
        private String deadline;
        private String department;
        private int views;

        public static Job from (JobPosting entity) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(), entity.getApplyEndDate().toLocalDate());
            String dDay = days < 0 ? "마감" : "D-" + days;

            String requireExp;

            if (entity.getCareerType().equals(CareerType.EXPERIENCED)) {

                requireExp = CareerType.EXPERIENCED.getLabel() + " ";

                if (entity.getMinExperience() != null) {
                    requireExp = requireExp + entity.getMinExperience() + "년차 ~ ";
                }

                if (entity.getMaxExperience() != null) {
                    requireExp = requireExp + entity.getMaxExperience() + "년차";
                }

            } else {
                requireExp = entity.getCareerType().getLabel();
            }

            return Job.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .summary(entity.getSummary())
                    .experience(requireExp)
                    .location(entity.getLocation())
                    .deadline(dDay)
                    .department(entity.getDepartment().getDuty().getJobGroup().getName())
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

        public static Jobs from (List<JobPosting> entities, Long totalElements, boolean last) {
            return Jobs.builder()
                    .jobs(entities.stream().map(Job::from).toList())
                    .last(last)
                    .totalElements(totalElements)
                    .build();
        }
    }
}