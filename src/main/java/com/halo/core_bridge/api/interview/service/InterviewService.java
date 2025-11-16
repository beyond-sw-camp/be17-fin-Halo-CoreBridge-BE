package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.repository.InterviewQueryRepository;
import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.service.ResumeService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.Create;
import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.Interviews;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ResumeService resumeService;
    private final InterviewQueryRepository interviewQueryRepository;

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
}
