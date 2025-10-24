package com.halo.core_bridge.api.schedule.jobposting.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class JobPostingScheduleDto {

    @Getter @Setter @ToString
    public static class Request {
        private Long jobPostingId;
        private String title;
        private String description;
        private String startDate; // yyyy-MM-dd
        private String endDate;   // yyyy-MM-dd
        private boolean allDay;
        private String frequency; // DAILY/WEEKLY/MONTHLY (optional)
        private Integer interval;  // optional
        private String recurringEndDate; // yyyy-MM-dd (optional)
        private String color; // optional
    }

    @Getter @Setter @ToString
    public static class Response {
        private Long id;
        private Long jobPostingId;
        private String title;
        private String description;
        private String date;     // yyyy-MM-dd (expanded each day)
        private boolean allDay;
        private String frequency; // DAILY/WEEKLY/MONTHLY (optional)
        private Integer interval;  // optional
        private String recurringEndDate; // yyyy-MM-dd (optional)
        private String color;
        private String recurringGroupId; // nullable
    }
}
