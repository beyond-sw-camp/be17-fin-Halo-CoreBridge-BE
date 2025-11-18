package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewQueryRepository {

    Page<InterviewDto.Read> search(InterviewDto.SearchQuery searchQuery, Pageable pageable);
    Page<InterviewDto.Read> searchV2(InterviewDto.SearchQuery searchQuery, Pageable pageable);

}
