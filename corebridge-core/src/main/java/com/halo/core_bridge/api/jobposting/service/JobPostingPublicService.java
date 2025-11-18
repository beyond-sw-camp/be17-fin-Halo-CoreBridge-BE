package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.repository.PublicJobPostingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobPostingPublicService {


    private final PublicJobPostingQueryRepository  publicJobPostingQueryRepository;


    // 지원자 입장 채용공고 전체조회(QueryDsl 기반)
    public PublicJobPostingDto.Jobs searchPublicJobs(PublicJobPostingDto.PublicJobSearchRequest req, Pageable pageable) {

        Page<PublicJobPostingDto.JobRaw> pageResult =
                publicJobPostingQueryRepository.searchPublicJobs(req, pageable);

        return PublicJobPostingDto.Jobs.from(
                pageResult.getContent(),
                pageResult.getTotalElements(),
                pageResult.isLast()
        );
    }
}
