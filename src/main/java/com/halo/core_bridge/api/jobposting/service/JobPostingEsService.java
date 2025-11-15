package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.document.JobPostingDocument;
import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.repository.es.JobPostingSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.JobPostingPage;

@Service
@RequiredArgsConstructor
public class JobPostingEsService {

    private final JobPostingSearchRepository jobPostingSearchRepository;

    public JobPostingPage searchJobPostings(JobPostingDto.SearchQuery searchQuery) {

        PageRequest pageable = PageRequest.of(searchQuery.getPage(), 10, Sort.by("id").descending());

        SearchHits<JobPostingDocument> results = jobPostingSearchRepository.search(searchQuery, pageable);

        List<JobPostingDocument> docs = results.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        long totalElements = results.getTotalHits();
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return JobPostingPage.fromJobPostingDocument(
                docs,
                currentPage,
                totalPages,
                totalElements
        );
    }
}
