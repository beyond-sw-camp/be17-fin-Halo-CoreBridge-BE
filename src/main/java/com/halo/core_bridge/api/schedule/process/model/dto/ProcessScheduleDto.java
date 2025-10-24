package com.halo.core_bridge.api.schedule.process.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ProcessScheduleDto {
    @Getter @Setter @ToString
    public static class Request {
        private Long jobPostingId;
        private String type;        // e.g., interview_1
        private String title;
        private String candidateName;
        private String startDate;   // yyyy-MM-dd
        private String endDate;     // yyyy-MM-dd
        private boolean recurring;  // create daily between range
        private String location;
        private String notes;
    }
    @Getter @Setter @ToString
    public static class Response {
        private Long id;
        private Long jobPostingId;
        private String type;
        private String title;
        private String candidateName;
        private String date;
        private String location;
        private String notes;
        private String recurringGroupId;
    }
}
