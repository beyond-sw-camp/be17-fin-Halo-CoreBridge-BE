package com.halo.core_bridge.api.schedule.jobprocess.model.dto;

import com.halo.core_bridge.api.schedule.jobprocess.model.enums.RecurrenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class JobProcessScheduleDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @NotBlank private String scheduleType;
        @NotBlank private String title;
        private String candidateName;
        private String position;

        @NotNull private LocalDate startDate;
        @NotNull private LocalDate endDate;
        @NotNull private LocalTime startTime;
        @NotNull private LocalTime endTime;

        private String location;
        @NotBlank private String priority;
        private String interviewer;
        private String notes;
        @NotBlank private String status; // scheduled, completed, cancelled

        // Recurring 필드
        private RecurrenceType recurrenceType;
        private Integer recurrenceInterval;
        private LocalDate recurrenceEndDate;

        @NotNull private Long assignedTo;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Update {
        private String scheduleType;
        private String title;
        private String candidateName;
        private String position;

        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;

        private String location;
        private String priority;
        private String interviewer;
        private String notes;
        private String status;

        // Recurring 필드
        private RecurrenceType recurrenceType;
        private Integer recurrenceInterval;
        private LocalDate recurrenceEndDate;

        private Long assignedTo;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String scheduleType;
        private String title;
        private String candidateName;
        private String position;

        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;

        private String location;
        private String priority;
        private String interviewer;
        private String notes;
        private String status;

        // Recurring 필드
        private RecurrenceType recurrenceType;
        private Integer recurrenceInterval;
        private LocalDate recurrenceEndDate;
        private Long parentScheduleId;

        private Long jobPostingId;
        private Long assignedTo;
        private List<Long> sharedWith;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ShareRequest {
        @NotNull private List<Long> userIds;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BulkShareRequest {
        @NotNull private List<Long> schedules;
        @NotNull private List<Long> members;
    }
}
