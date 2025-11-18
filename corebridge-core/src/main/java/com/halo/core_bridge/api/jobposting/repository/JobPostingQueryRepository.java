package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobPostingQueryRepository {
    List<JobPostingDto.JobPostingListResponseDto> findAllJobPostingSummaries();

    JobPostingDto.DetailResponse findJobPostingDetail(Long jobPosingId);

    Page<JobPosting> searchJobPostings(JobPostingDto.SearchQuery keyword, Pageable pageable);
}
