package com.halo.core_bridge.api.resume.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class ResumeSearchDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;
        private Long jobPostingId;
        private String degree;
        private List<String> skills;
        private String companyName;
        private String certificateName;
        private String country;

        @Builder.Default
        private Integer page = 0;

        @Builder.Default
        private Integer size = 20;

        @Builder.Default
        private String sortBy = "appliedAt";

        @Builder.Default
        private String sortDirection = "desc";
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private Long id;
        private OffsetDateTime appliedAt;
        private String description;
        private String userName;
        private String userEmail;
        private String userPhone;
        private Long jobPostingId;
        private String jobPostingTitle;
        private List<CareerSummary> careers;
        private List<EducationSummary> educations;
        private List<String> skills;
        private Integer certificateCount;
        private Float score;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerSummary {
        private String companyName;
        private String position;
        private OffsetDateTime startDate;
        private OffsetDateTime endDate;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationSummary {
        private String schoolName;
        private String major;
        private String degree;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResponse {
        private List<SearchResponse> content;
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
        private Boolean last;
    }
}