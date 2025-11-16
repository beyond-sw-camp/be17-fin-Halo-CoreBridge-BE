package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.model.dto.InterviewerDto;
import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import com.halo.core_bridge.api.interview.repository.InterviewQueryRepository;
import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import com.halo.core_bridge.api.interview.repository.InterviewerRepository;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.api.resume.service.ResumeService;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.Create;
import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.Interviews;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ResumeService resumeService;
    private final InterviewQueryRepository interviewQueryRepository;

    private final ResumeRepository resumeRepository;
    private final InterviewerRepository interviewerRepository;
    private final RecruitProcessRepository recruitProcessRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Create create) {

        if (interviewRepository.existsByRecruitProcess_IdAndResume_Id((create.getRecruiterProcessId()), create.getResumeId())) {
            throw BaseException.from(BaseResponseStatus.ALREADY_SCHEDULED_INTERVIEW);
        }

        Long resumeProcessId;

        try {

            Resume findResume = resumeService.findById(create.getResumeId());
            resumeProcessId = findResume.getProcess().getId();

        } catch (RuntimeException e) {
            throw BaseException.from(BaseResponseStatus.RESUME_NOT_FOUND);
        }

        if (!resumeProcessId.equals(create.getRecruiterProcessId())) {
            throw BaseException.from(BaseResponseStatus.RESUME_PROCESS_NOT_MATCH);
        }

        Interview savedInterview = interviewRepository.save(create.toEntity());
        return savedInterview.getId();
    }

    public Interviews search(InterviewDto.SearchQuery searchQuery) {

        PageRequest pageable = PageRequest.of(searchQuery.getPage(), 10, Sort.by("id").descending());

        Page<InterviewDto.Read> search = interviewQueryRepository.search(searchQuery, pageable);
        return Interviews.fromSearch(search.getContent(), search.getNumber(), search.getTotalPages(), search.getTotalElements());
    }

    public InterviewDto.Read findByInterviewId(Long interviewId) {

        Interview findInterview = interviewRepository.findById(interviewId).orElseThrow(() -> BaseException.from(BaseResponseStatus.INTERVIEW_NOT_FOUND));

        Resume findResume = resumeRepository.findById(findInterview.getResume().getId())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.RESUME_NOT_FOUND));

        User findUser = userRepository.findById(findResume.getUser().getId())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        List<Interviewer> findInterviewers = interviewerRepository.findAllByJobPosting_Id(findResume.getJobPosting().getId());

        RecruitProcess findProcess = recruitProcessRepository.findById(findInterview.getRecruitProcess().getId())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.RECRUIT_PROCESS_NOT_FOUND));

        return InterviewDto.Read.builder()
                .id(findInterview.getId())
                .name(findUser.getName())
                .startDateTime(findInterview.getStartDateTime())
                .duration(findInterview.getDuration())
                .process(findProcess.getName())
                .interviewType(findInterview.getInterviewType())
                .location(findInterview.getLocation())
                .interviewStatus(findInterview.getStatus())
                .description(findInterview.getDescription())
                .interviewers(
                        findInterviewers.stream().map(InterviewerDto.InterviewerInfo::from).toList()
                )
                .build();
    }
}
