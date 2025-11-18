package com.halo.core_bridge.api.resume.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.halo.core_bridge.api.resume.document.ResumeDocument;
import com.halo.core_bridge.api.resume.model.dto.ResumeSearchDto;
import com.halo.core_bridge.api.resume.repository.ResumeSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeSearchService {

    private final ResumeSearchRepository resumeSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public ResumeSearchDto.PageResponse search(ResumeSearchDto.SearchRequest request) {
        NativeQuery searchQuery = buildSearchQuery(request);

        SearchHits<ResumeDocument> searchHits = elasticsearchOperations.search(
                searchQuery,
                ResumeDocument.class
        );

        List<ResumeSearchDto.SearchResponse> content = searchHits.getSearchHits().stream()
                .map(this::toSearchResponse)
                .collect(Collectors.toList());

        long totalHits = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalHits / request.getSize());

        return ResumeSearchDto.PageResponse.builder()
                .content(content)
                .page(request.getPage())
                .size(request.getSize())
                .totalElements(totalHits)
                .totalPages(totalPages)
                .last(request.getPage() >= totalPages - 1)
                .build();
    }

    private NativeQuery buildSearchQuery(ResumeSearchDto.SearchRequest request) {
        List<Query> mustQueries = new ArrayList<>();
        List<Query> shouldQueries = new ArrayList<>();
        List<Query> filterQueries = new ArrayList<>();

        // 키워드 검색
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            shouldQueries.add(Query.of(q -> q
                    .match(m -> m
                            .field("allText")
                            .query(request.getKeyword())
                            .boost(1.0f)
                    )
            ));

            shouldQueries.add(Query.of(q -> q
                    .match(m -> m
                            .field("userName")
                            .query(request.getKeyword())
                            .boost(2.0f)
                    )
            ));

            shouldQueries.add(Query.of(q -> q
                    .match(m -> m
                            .field("skills")
                            .query(request.getKeyword())
                            .boost(1.5f)
                    )
            ));
        }

        // 채용 공고 ID 필터
        if (request.getJobPostingId() != null) {
            filterQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("jobPostingId")
                            .value(request.getJobPostingId())
                    )
            ));
        }

        // 학위 필터
        if (request.getDegree() != null && !request.getDegree().isEmpty()) {
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("educations")
                            .query(nq -> nq
                                    .term(t -> t
                                            .field("educations.degree")
                                            .value(request.getDegree())
                                    )
                            )
                    )
            ));
        }

        // 기술 스택 필터
        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            List<Query> skillQueries = request.getSkills().stream()
                    .map(skill -> Query.of(q -> q
                            .term(t -> t
                                    .field("skills")
                                    .value(skill)
                            )
                    ))
                    .collect(Collectors.toList());

            filterQueries.add(Query.of(q -> q
                    .bool(b -> b.should(skillQueries))
            ));
        }

        // 회사명 필터
        if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("careers")
                            .query(nq -> nq
                                    .match(m -> m
                                            .field("careers.companyName")
                                            .query(request.getCompanyName())
                                    )
                            )
                    )
            ));
        }

        // 자격증명 필터
        if (request.getCertificateName() != null && !request.getCertificateName().isEmpty()) {
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("certificates")
                            .query(nq -> nq
                                    .match(m -> m
                                            .field("certificates.name")
                                            .query(request.getCertificateName())
                                    )
                            )
                    )
            ));
        }

        // 해외 경험 국가 필터
        if (request.getCountry() != null && !request.getCountry().isEmpty()) {
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("overseasExperiences")
                            .query(nq -> nq
                                    .term(t -> t
                                            .field("overseasExperiences.country")
                                            .value(request.getCountry())
                                    )
                            )
                    )
            ));
        }

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (!shouldQueries.isEmpty()) {
            boolQueryBuilder.should(shouldQueries);
            boolQueryBuilder.minimumShouldMatch("1");
        }

        if (!mustQueries.isEmpty()) {
            boolQueryBuilder.must(mustQueries);
        }

        if (!filterQueries.isEmpty()) {
            boolQueryBuilder.filter(filterQueries);
        }

        Query finalQuery;
        if (shouldQueries.isEmpty() && mustQueries.isEmpty() && filterQueries.isEmpty()) {
            finalQuery = Query.of(q -> q.matchAll(m -> m));
        } else {
            finalQuery = Query.of(q -> q.bool(boolQueryBuilder.build()));
        }

        Sort.Direction direction = request.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(direction, request.getSortBy())
        );

        return NativeQuery.builder()
                .withQuery(finalQuery)
                .withPageable(pageRequest)
                .build();
    }

    private ResumeSearchDto.SearchResponse toSearchResponse(SearchHit<ResumeDocument> searchHit) {
        ResumeDocument doc = searchHit.getContent();

        List<ResumeSearchDto.CareerSummary> careers = doc.getCareers() != null
                ? doc.getCareers().stream()
                .map(c -> ResumeSearchDto.CareerSummary.builder()
                        .companyName(c.getCompanyName())
                        .position(c.getPosition())
                        .startDate(c.getStartDate())
                        .endDate(c.getEndDate())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        List<ResumeSearchDto.EducationSummary> educations = doc.getEducations() != null
                ? doc.getEducations().stream()
                .map(e -> ResumeSearchDto.EducationSummary.builder()
                        .schoolName(e.getSchoolName())
                        .major(e.getMajor())
                        .degree(e.getDegree())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        return ResumeSearchDto.SearchResponse.builder()
                .id(Long.parseLong(doc.getId()))
                .appliedAt(doc.getAppliedAt())
                .description(doc.getDescription())
                .userName(doc.getUserName())
                .userEmail(doc.getUserEmail())
                .userPhone(doc.getUserPhone())
                .jobPostingId(doc.getJobPostingId())
                .jobPostingTitle(doc.getJobPostingTitle())
                .careers(careers)
                .educations(educations)
                .skills(doc.getSkills())
                .certificateCount(doc.getCertificates() != null ? doc.getCertificates().size() : 0)
                .score(searchHit.getScore())
                .build();
    }
}