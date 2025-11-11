package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.document.JobPostingDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobPostingEsRepository extends ElasticsearchRepository<JobPostingDoc, Long> {

    Page<JobPostingDoc> findAllByTitle(String title, Pageable pageable);
}
