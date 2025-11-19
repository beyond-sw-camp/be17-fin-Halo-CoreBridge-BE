package com.halo.core_bridge.api.interview.model.entity;

import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_interview_id", columnList = "interview_id")
        }
)
public class InterviewAssignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 배정이 속한 면접
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    // 이 면접에 참여하는 면접관
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_user_id", nullable = false)
    private User interviewer;
}
