package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingPublicService {

    private final JobPostingRepository jobPostingRepository;

    /**
     * 지원자가 보는 채용 공고 리스트를 조회한다.
     * @return <code>PublicJobPostingDto.Jobs</code> - 조회 DTO
     */
    public PublicJobPostingDto.Jobs findAllJobs() {

        List<JobPosting> jobs = jobPostingRepository.findAll();
        return PublicJobPostingDto.Jobs.from(jobs);
    }
}
