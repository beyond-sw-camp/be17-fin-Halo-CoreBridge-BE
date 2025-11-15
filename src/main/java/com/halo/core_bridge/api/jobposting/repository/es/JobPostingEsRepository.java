package com.halo.core_bridge.api.jobposting.repository.es;

import com.halo.core_bridge.api.jobposting.model.document.JobPostingDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobPostingEsRepository extends ElasticsearchRepository<JobPostingDocument, Long> {

    Page<JobPostingDocument> findAllByTitle(String title, Pageable pageable);
}
