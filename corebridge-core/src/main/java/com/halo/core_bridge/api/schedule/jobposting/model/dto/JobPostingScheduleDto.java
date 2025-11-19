package com.halo.core_bridge.api.schedule.jobposting.model.dto;

import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingSchedule;
import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingScheduleShare;
import com.halo.core_bridge.api.schedule.jobposting.model.enums.JobPostingStatus;
import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class JobPostingScheduleDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @NotBlank private String title;
        @NotBlank private String position;
        @NotBlank private String department;
        @NotBlank private String experience;
        @NotBlank private String type;
        @NotNull  private Long assignedTo;
        @NotNull  private LocalDate postedDate;
        @NotNull  private LocalDate deadline;
        private LocalTime startTime;
        private LocalTime endTime;
        @NotNull  private JobPostingStatus status;
        private String description;
        private String responsibilities;
        private String requirements;
        private String preferences;
        private String benefits;
        private boolean isUrgent;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Update {
        @NotBlank private String title;
        @NotBlank private String position;
        @NotBlank private String department;
        @NotBlank private String experience;
        @NotBlank private String type;
        @NotNull  private Long assignedTo;
        @NotNull  private LocalDate postedDate;
        @NotNull  private LocalDate deadline;
        private LocalTime startTime;
        private LocalTime endTime;
        @NotNull  private JobPostingStatus status;
        private String description;
        private String responsibilities;
        private String requirements;
        private String preferences;
        private String benefits;
        private boolean isUrgent;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String title;
        private String position;
        private String department;
        private String experience;
        private String type;
        private Long assignedTo;
        private LocalDate postedDate;
        private LocalDate deadline;
        private LocalTime startTime;
        private LocalTime endTime;
        private JobPostingStatus status;
        private String description;
        private String responsibilities;
        private String requirements;
        private String preferences;
        private String benefits;
        private boolean isUrgent;
        private int applicants;
        private int progress;
        private int screening;
        private int interview1;
        private int interview2;
        private int finalStage;
        private List<Long> sharedWith;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ShareRequest {
        @NotNull private List<Long> userIds;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CalendarItem {
        private Long id;
        private String title;
        private JobPostingStatus status;
        private boolean urgent;
        private String department;
    }

    // ===== Mapping helpers =====
    public static Response toDto(JobPostingSchedule e) {
        return Response.builder()
                .id(e.getId())
                .title(e.getTitle())
                .position(e.getPosition())
                .department(e.getDepartment())
                .experience(e.getExperience())
                .type(e.getType())
                .assignedTo(e.getAssignedTo() != null ? e.getAssignedTo().getId() : null)
                .postedDate(e.getPostedDate())
                .deadline(e.getDeadline())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .status(e.getStatus())
                .description(e.getDescription())
                .responsibilities(e.getResponsibilities())
                .requirements(e.getRequirements())
                .preferences(e.getPreferences())
                .benefits(e.getBenefits())
                .isUrgent(e.isUrgent())
                .applicants(e.getApplicants())
                .progress(e.getProgress())
                .screening(e.getScreening())
                .interview1(e.getInterview1())
                .interview2(e.getInterview2())
                .finalStage(e.getFinalStage())
                .sharedWith(
                        e.getShares() == null ? List.of() :  // null 방어
                                e.getShares().stream()
                                        .map(JobPostingScheduleShare::getUser)
                                        .filter(Objects::nonNull) // null user 방어
                                        .map(User::getId)
                                        .toList()
                )
                .build();
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BulkShareRequest {
        @NotNull
        private List<Long> jobs;    // 공유할 공고 ID 배열
        @NotNull
        private List<Long> members; // 공유 대상 유저 ID 배열
    }



}
