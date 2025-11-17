package com.halo.core_bridge.api.ai.dto;

import java.util.List;

public class AiDtos {
    public record TextInput(String text) {}
    public record ResumeSaveReq(String candidate_id, String resume_text) {}
    public record MatchRequest(String jd_text, List<String> required_skills, int top_k) {}
    public record ScoreRequest(String jd_text, String candidate_id, List<String> required_skills) {}
    public record SummaryRes(String summary) {}
    public record SkillsRes(List<String> skills) {}
    public record MatchHit(String key, double score) {}
    public record MatchRes(List<MatchHit> matches) {}

    public static class ScoreDetail {
        public double skill_ratio;
        public double skill_score;
        public double sim_score;
        public double bonus;
        public double total;
    }

    public static class ScoreRes {
        public String candidate_id;
        public List<String> required_skills;
        public List<String> candidate_skills;
        public double cosine_similarity;
        public ScoreDetail score_detail;
    }
}
