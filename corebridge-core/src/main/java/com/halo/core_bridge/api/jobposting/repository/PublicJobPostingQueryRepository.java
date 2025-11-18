package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicJobPostingQueryRepository {
    Page<PublicJobPostingDto.JobRaw> searchPublicJobs(
            PublicJobPostingDto.PublicJobSearchRequest req,
            Pageable pageable
    );
}
