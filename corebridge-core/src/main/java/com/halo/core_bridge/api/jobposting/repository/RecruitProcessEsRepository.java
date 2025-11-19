package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.document.RecruitProcessDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RecruitProcessEsRepository extends ElasticsearchRepository<RecruitProcessDoc, Long> {

    List<RecruitProcessDoc> findByJobPostingId(Long jobPostingId);
}
