package com.halo.core_bridge.api.personal.model.dto;

import lombok.*;

import java.util.List;

public class PersonalDataDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long userId;          // 사용자 ID 추가 ✨
        private Long resumeId;
        private String summary;
        private List<String> skills;
        private List<RecommendResult> recommendResults;
        private ScoreDetail scoreDetail;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecommendResult {
        private String candidateId;  // "candidate:65"
        private Double score;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScoreDetail {
        private Double cosineSimilarity;
        private Double skillRatio;
        private Double skillScore;
        private Double simScore;
        private Double bonus;
        private Double totalScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long userId;          // 사용자 ID 추가 ✨
        private Long resumeId;
        private String summary;
        private List<String> skills;
        private List<RecommendResult> recommendResults;
        private ScoreDetail scoreDetail;
        private String grade;
        private Boolean isMatched;
    }
}
