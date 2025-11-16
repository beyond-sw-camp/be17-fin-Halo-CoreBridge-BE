package com.halo.core_bridge.api.jobposting.repository.es;

import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.halo.core_bridge.api.jobposting.model.document.JobPostingDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.SearchQuery;

@Repository
@RequiredArgsConstructor
public class JobPostingSearchOperationRepository implements JobPostingSearchRepository{

    private final ElasticsearchOperations operations;

    @Override
    public SearchHits<JobPostingDocument> search(SearchQuery searchQuery, Pageable pageable) {

        String keyword = searchQuery.getKeyword();

        NativeQuery query;
        if (keyword == null || keyword.isBlank()) {
            // 키워드 없으면 전체 조회
            query = new NativeQueryBuilder()
                    .withQuery(q ->
                            q.matchAll(
                                    ma -> ma
                            ))
                    .withSort(
                            Sort.by(
                                    Sort.Order.asc("apply_start_date")
                            )
                    )
                    .withPageable(pageable)
                    .withTrackTotalHits(true)
                    .build();
        } else {
            query = new NativeQueryBuilder()
                    .withQuery(root ->
                            root.bool(bool ->
                                    bool.must(must ->
                                            must.multiMatch(mm ->
                                                    mm.query(keyword)
                                                            .type(TextQueryType.MostFields)
                                                            .fields("title^3")
                                                            .fields("title.ngram^4")
                                                            .fields("summary^5")
                                                            .fields("preferred^6")
                                                            .fields("requirements^7")
                                            )
                                    )
                            )
                    )
                    .withSort(
                            Sort.by(
                                    Sort.Order.desc("_score"),
                                    Sort.Order.asc("apply_start_date")
                            ))
                    .withPageable(pageable)
                    .withTrackTotalHits(true)
                    .build();
        }

        return operations.search(query, JobPostingDocument.class);
    }
}
