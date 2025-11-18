package com.halo.core_bridge.api.evaluation.service;

import com.halo.core_bridge.api.evaluation.model.dto.EvaluationDto;
import com.halo.core_bridge.api.evaluation.model.entity.Evaluation;
import com.halo.core_bridge.api.evaluation.model.entity.EvaluationCriteriaScore;
import com.halo.core_bridge.api.evaluation.repository.EvaluationCriteriaScoreRepository;
import com.halo.core_bridge.api.evaluation.repository.EvaluationRepository;
import com.halo.core_bridge.api.interview.model.entity.InterviewAssignment;
import com.halo.core_bridge.api.interview.repository.InterviewAssignmentRepository;
import com.halo.core_bridge.api.interview.service.InterviewService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ConcreteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConcreteProxy
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationCriteriaScoreRepository criteriaScoreRepository;
    private final InterviewAssignmentRepository interviewAssignmentRepository;
    private final InterviewService interviewService;

    @Transactional
    public void save(EvaluationDto.Create createEvaluation, Long userId) {

        InterviewAssignment findInterviewer = interviewAssignmentRepository.findByInterview_IdAndInterviewer_Id(
                createEvaluation.getInterviewId(),
                        userId
                )
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_INTERVIEW_ASSIGNMENT));

        // 기존 평가가 있는지 체크
        if (evaluationRepository.existsEvaluationByAssignment_Id(findInterviewer.getId())) {
            throw BaseException.from(BaseResponseStatus.CANNOT_MODIFY_EVALUATION);
        }

        // 평균 점수 계산
        double avg = createEvaluation.getEvaluationScores().stream()
                .mapToInt(EvaluationDto.EvaluationScoreCreate::getScore)
                .average()
                .orElse(0.0);

        Evaluation savedEvaluation = evaluationRepository.save(createEvaluation.toEntity(avg, findInterviewer.getId()));

        // 새로운 항목 점수 저장
        for (EvaluationDto.EvaluationScoreCreate c : createEvaluation.getEvaluationScores()) {

            EvaluationCriteriaScore evaluationCriteriaScore = c.toEvaluationCriteriaScore(savedEvaluation);
            criteriaScoreRepository.save(evaluationCriteriaScore);
        }

        interviewService.checkAutoEnd(createEvaluation.getInterviewId());
    }

    // -------------------------------------------------------------------------------------

    /** 면접 전체 평가 요약 */
//    public EvaluationSummaryResponse getSummary(Long interviewId) {
//
//        List<InterviewAssignment> assignments =
//                assignmentRepository.findByInterviewId(interviewId);
//
//        EvaluationSummaryResponse summary = new EvaluationSummaryResponse();
//        summary.setInterviewId(interviewId);
//
//        List<EvaluationSummaryResponse.InterviewerEvaluation> evalList = new ArrayList<>();
//
//        double total = 0;
//        int count = 0;
//
//        for (InterviewAssignment a : assignments) {
//
//            Optional<Evaluation> evOpt = evaluationRepository.findByAssignmentId(a.getId());
//            if (evOpt.isEmpty()) continue;
//
//            Evaluation ev = evOpt.get();
//            total += ev.getAvgScore();
//            count++;
//
//            EvaluationSummaryResponse.InterviewerEvaluation item =
//                    new EvaluationSummaryResponse.InterviewerEvaluation();
//
//            item.setInterviewerId(a.getInterviewerId());
//            item.setAvgScore(ev.getAvgScore());
//
//            // 항목별 점수 조회
//            List<EvaluationCriteriaScore> scores =
//                    criteriaScoreRepository.findByEvaluationId(ev.getId());
//
//            item.setCriteriaScores(
//                    scores.stream()
//                            .map(s -> new EvaluationSummaryResponse.CriteriaScore(
//                                    s.getCriteriaId(),
//                                    s.getScore()
//                            )).toList()
//            );
//
//            evalList.add(item);
//        }
//
//        summary.setEvaluations(evalList);
//        summary.setAverageScore(count == 0 ? 0.0 : total / count);
//
//        return summary;
//    }
}
