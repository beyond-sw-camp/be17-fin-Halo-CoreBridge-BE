package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import com.halo.core_bridge.api.interview.repository.InterviewerRepository;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.users.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.halo.core_bridge.api.interview.model.dto.InterviewerDto.InterviewerList;

@Service
@RequiredArgsConstructor
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;

    /**
     * 특정 채용 공고에 면접관 리스트 배정
     * @param jobPostingId 채용 공고 Id
     * @param interviewerIds 면접관 Id 리스트
     */
    @Transactional
    public void saveAll(Long jobPostingId, List<Long> interviewerIds) {

        List<Interviewer> interviewers = interviewerIds.stream().map(interviewerId ->
                        Interviewer.builder()
                                .jobPosting(
                                        JobPosting.builder().id(jobPostingId).build()
                                )
                                .user(
                                        User.builder().id(interviewerId).build()
                                )
                                .build()
                )
                .toList();

        interviewerRepository.saveAll(interviewers);
    }


    public InterviewerList findInterviewersByJobPostingId(Long JobPostingId) {

        List<Interviewer> findAll = interviewerRepository.findAllByJobPosting_Id((JobPostingId));
        return InterviewerList.from(findAll);
    }
}
