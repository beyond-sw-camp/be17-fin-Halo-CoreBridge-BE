package com.halo.core_bridge.api.evaluation.model.dto;

import com.halo.core_bridge.api.evaluation.model.entity.Evaluation;
import com.halo.core_bridge.api.evaluation.model.entity.EvaluationCriteria;
import com.halo.core_bridge.api.evaluation.model.entity.EvaluationCriteriaScore;
import com.halo.core_bridge.api.interview.model.entity.InterviewAssignment;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class EvaluationDto {

    @Getter
    @Builder
    public static class Read {

        private Long id;
        private String title;
        private String description;

        public static EvaluationDto.Read from(EvaluationCriteria entity) {
            return Read.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EvaluationCriteriaList {

        List<Read> evaluationTemplates;

        public static EvaluationCriteriaList from(List<EvaluationCriteria> entityList) {
            return EvaluationCriteriaList.builder()
                    .evaluationTemplates(entityList.stream().map(EvaluationDto.Read::from).toList())
                    .build();

        }
    }

    @Getter
    public static class EvaluationScoreCreate {

        @NotNull(message = "평가 항목은  ID는 필수입니다.")
        private Long id;

        @NotNull(message = "점수는 필수입니다.")
        private Integer score;

        @NotNull(message = "평가 내용은 필수입니다.")
        private String comment;

        public EvaluationCriteriaScore toEvaluationCriteriaScore(Evaluation evaluation) {

            return EvaluationCriteriaScore.builder()
                    .evaluation(evaluation)
                    .criteria(
                            EvaluationCriteria.builder().id(id).build()
                    )
                    .score(this.score)
                    .comment(this.comment)
                    .build();
        }
    }

    @Getter
    public static class Create {

        @NotNull(message = "인터뷰 ID는 필수입니다.")
        private Long interviewId;   // 어떤 면접관의 평가인지

        @NotNull(message = "총평을 입력해주세요.")
        private String overallComment; // 총평

        @NotNull(message = "항목별 점수를 입력해주세요.")
        private List<EvaluationScoreCreate> evaluationScores; // 항목별 점수

        public Evaluation toEntity(Double avgScore, Long assignmentId) {

            return Evaluation.builder()
                    .assignment(
                            InterviewAssignment.builder().id(assignmentId).build()
                    )
                    .avgScore(avgScore)
                    .overallComment(overallComment)
                    .build();
        }
    }
}
