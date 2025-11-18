package com.halo.core_bridge.api.personal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class N8nAnalysisDto {

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
        private Long workflowMs;

        @JsonProperty("recommend")  // 원본 데이터의 오타 그대로 매핑
        private List<RecommendItem> recommend;

        private ScoreResult scoreResult;

        // processedAt은 무시
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecommendItem {
        @JsonProperty("key")
        private String key;  // "candidate:65" 형식

        @JsonProperty("score")
        private Double score;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScoreResult {
        @JsonProperty("cosine_similarity")
        private Double cosineSimilarity;

        @JsonProperty("skill_ratio")
        private Double skillRatio;

        @JsonProperty("skill_score")
        private Double skillScore;

        @JsonProperty("sim_score")
        private Double simScore;

        @JsonProperty("bonus")
        private Double bonus;

        @JsonProperty("total")
        private Double total;
    }
}
