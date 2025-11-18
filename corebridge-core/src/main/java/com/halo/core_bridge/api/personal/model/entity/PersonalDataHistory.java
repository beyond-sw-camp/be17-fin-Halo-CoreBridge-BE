package com.halo.core_bridge.api.personal.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personal_data_history",
        indexes = {
                @Index(name = "idx_history_user_id", columnList = "userId"),
                @Index(name = "idx_history_resume_id", columnList = "resumeId"),
                @Index(name = "idx_history_user_resume", columnList = "userId, resumeId")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PersonalDataHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;                // 사용자 ID (추가)

    private Long resumeId;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String recommendResults;

    private Double cosineSimilarity;
    private Double skillRatio;
    private Double skillScore;
    private Double simScore;
    private Double bonus;
    private Double totalScore;
}
