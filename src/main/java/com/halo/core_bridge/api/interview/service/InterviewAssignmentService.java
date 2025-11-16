package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.entity.InterviewAssignment;
import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import com.halo.core_bridge.api.interview.repository.InterviewAssignmentRepository;
import com.halo.core_bridge.api.interview.repository.InterviewerRepository;
import com.halo.core_bridge.api.users.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewAssignmentService {

    private final InterviewAssignmentRepository assignmentRepository;
    private final InterviewerRepository interviewerRepository;

    /**
     * Interview 생성 직후 호출해서
     * - 공고별 면접관 풀 기준으로 InterviewAssignment 생성
     * - 공고별 평가항목 기준으로 Score row 미리 만들어두기 (선택)
     */
    @Transactional
    public void createAssignmentsForInterview(Interview interview, Long jobPostingId) {

        if (jobPostingId == null) {
            return; // 방어코드
        }

        // 1) 공고에 등록된 면접관 풀 조회
        List<Interviewer> postingInterviewers = interviewerRepository.findAllByJobPosting_Id(jobPostingId);

        // 2) 공고에 등록된 평가 항목 조회
//        List<EvaluationItem> evaluationItems =
//                evaluationItemRepository.findByJobPosting(jobPosting);

        for (Interviewer postingInterviewer : postingInterviewers) {
            User interviewerUser = postingInterviewer.getUser();

            // Assignment 생성
            InterviewAssignment assignment = InterviewAssignment.builder()
                    .interview(interview)
                    .interviewer(interviewerUser)
//                    .submitted(false)
                    .build();

            assignmentRepository.save(assignment);

            // 평가 항목별 Score row 미리 생성 (선택사항)
//            for (EvaluationItem item : evaluationItems) {
//                InterviewAssignmentScore score = InterviewAssignmentScore.builder()
//                        .assignment(assignment)
//                        .evaluationItem(item)
//                        .score(0)          // 초기값 (0 또는 null)
//                        .comment(null)
//                        .build();
//                scoreRepository.save(score);
//            }
        }
    }
}
