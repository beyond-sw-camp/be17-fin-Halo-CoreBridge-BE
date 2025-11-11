package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;

import java.util.List;
import java.util.Optional;

public interface JobPostingQueryRepository {
    List<JobPostingDto.JobPostingListResponseDto> findAllJobPostingSummaries();

    JobPostingDto.DetailResponse findJobPostingDetail(Long jobPosingId);
}
