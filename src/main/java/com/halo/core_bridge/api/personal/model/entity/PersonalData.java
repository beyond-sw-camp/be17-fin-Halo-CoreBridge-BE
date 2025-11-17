package com.halo.core_bridge.api.personal.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personal_data",
        indexes = {
                @Index(name = "idx_personal_data_user_id", columnList = "userId"),
                @Index(name = "idx_personal_data_resume_id", columnList = "resumeId"),
                @Index(name = "idx_personal_data_user_resume", columnList = "userId, resumeId")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PersonalData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;                // 사용자 ID (추가)

    @Column(nullable = false)
    private Long resumeId;              // 이력서 ID

    @Column(columnDefinition = "TEXT")
    private String summary;             // AI 요약

    @Column(columnDefinition = "TEXT")
    private String skills;              // JSON 배열 형태로 저장 ["Java", "Spring"]

    @Column(columnDefinition = "TEXT")
    private String recommendResults;    // JSON 형태로 저장 (추천 결과 목록)

    // 점수 상세 정보
    private Double cosineSimilarity;    // 코사인 유사도
    private Double skillRatio;          // 스킬 매칭 비율
    private Double skillScore;          // 스킬 점수
    private Double simScore;            // 유사도 점수
    private Double bonus;               // 보너스 점수
    private Double totalScore;          // 최종 점수

    // 헬퍼 메서드: 점수 기반 등급 반환
    public String getGrade() {
        if (totalScore == null) return "N/A";
        if (totalScore >= 80) return "S";
        if (totalScore >= 60) return "A";
        if (totalScore >= 40) return "B";
        if (totalScore >= 20) return "C";
        return "D";
    }

    // 헬퍼 메서드: 매칭 여부 판단
    public boolean isMatched() {
        return totalScore != null && totalScore >= 60.0;
    }

    // 비즈니스 로직: 데이터 업데이트
    public void updateAnalysisResult(
            String summary,
            String skills,
            String recommendResults,
            Double cosineSimilarity,
            Double skillRatio,
            Double skillScore,
            Double simScore,
            Double bonus,
            Double totalScore) {

        this.summary = summary;
        this.skills = skills;
        this.recommendResults = recommendResults;
        this.cosineSimilarity = cosineSimilarity;
        this.skillRatio = skillRatio;
        this.skillScore = skillScore;
        this.simScore = simScore;
        this.bonus = bonus;
        this.totalScore = totalScore;
    }
}
