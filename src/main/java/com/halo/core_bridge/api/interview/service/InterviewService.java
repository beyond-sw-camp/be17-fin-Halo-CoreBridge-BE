package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public Long save(InterviewDto.Create create) {

        Interview savedInterview = interviewRepository.save(create.toEntity());
        return savedInterview.getId();
    }
}
