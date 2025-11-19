package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.document.ResumeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ResumeSearchRepository extends ElasticsearchRepository<ResumeDocument, String> {
}
