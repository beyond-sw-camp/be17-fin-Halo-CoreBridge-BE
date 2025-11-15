package com.halo.core_bridge.api.jobposting.repository.es;

import com.halo.core_bridge.api.jobposting.model.document.JobPostingDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.SearchQuery;

public interface JobPostingSearchRepository {

    SearchHits<JobPostingDocument> search(SearchQuery query, Pageable pageable);

}
