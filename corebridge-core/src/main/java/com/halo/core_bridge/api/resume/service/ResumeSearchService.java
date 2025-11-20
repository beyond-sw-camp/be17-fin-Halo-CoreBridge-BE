package com.halo.core_bridge.api.resume.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.halo.core_bridge.api.resume.document.ResumeDocument;
import com.halo.core_bridge.api.resume.model.dto.ResumeSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    // ✅ SearchRequest 객체를 받는 메서드 (Controller에서 호출)
    public ResumeSearchDto.PageResponse search(ResumeSearchDto.SearchRequest request) {
        log.info("=== Elasticsearch 검색 시작 (SearchRequest) ===");
        log.info("Request: {}", request);

        // Pageable 생성
        Sort sort = Sort.by(
                request.getSortDirection().equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy()
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        // 기존 메서드 호출
        Page<ResumeDocument> page = searchresume(
                request.getJobPostingId(),
                request.getKeyword(),
                request.getDegree(),
                request.getSkills(),
                request.getCompanyName(),
                request.getCertificateName(),
                request.getCountry(),
                pageable
        );

        // PageResponse 변환
        return convertToPageResponse(page);
    }

    // ✅ 기존 메서드 (그대로 유지)
    public Page<ResumeDocument> searchresume(
            Long jobPostingId,
            String keyword,
            String degree,
            List<String> skills,
            String companyName,
            String certificateName,
            String country,
            Pageable pageable
    ) {
        log.info("=== Elasticsearch 검색 시작 ===");
        log.info("jobPostingId: {}", jobPostingId);
        log.info("keyword: {}", keyword);
        log.info("degree: {}", degree);
        log.info("skills: {}", skills);
        log.info("companyName: {}", companyName);
        log.info("certificateName: {}", certificateName);
        log.info("country: {}", country);

        List<Query> mustQueries = new ArrayList<>();
        List<Query> filterQueries = new ArrayList<>();

        // 1. jobPostingId 필수 조건
        mustQueries.add(Query.of(q -> q
                .term(t -> t
                        .field("jobPostingId")
                        .value(jobPostingId)
                )
        ));

        // 2. keyword 검색 (allText, skills, userName, userEmail)
        if (keyword != null && !keyword.trim().isEmpty()) {
            log.info("✅ keyword 검색 조건 추가: {}", keyword);

            List<Query> shouldQueries = new ArrayList<>();

            // allText 검색
            shouldQueries.add(Query.of(q -> q
                    .match(m -> m
                            .field("allText")
                            .query(keyword)
                    )
            ));

            // skills 검색 (keyword 타입이므로 term 쿼리 사용)
            shouldQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("skills")
                            .value(keyword)
                    )
            ));

            // userName 검색
            shouldQueries.add(Query.of(q -> q
                    .match(m -> m
                            .field("userName")
                            .query(keyword)
                    )
            ));

            // userEmail 검색
            shouldQueries.add(Query.of(q -> q
                    .match(m -> m
                            .field("userEmail")
                            .query(keyword)
                    )
            ));

            mustQueries.add(Query.of(q -> q
                    .bool(b -> b
                            .should(shouldQueries)
                            .minimumShouldMatch("1")
                    )
            ));
        }

        // 3. degree 필터
        if (degree != null && !degree.trim().isEmpty()) {
            log.info("✅ degree 필터 추가: {}", degree);
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("educations")
                            .query(nq -> nq
                                    .match(m -> m
                                            .field("educations.degree")
                                            .query(degree)
                                    )
                            )
                    )
            ));
        }

        // 4. skills 필터 (keyword 타입이므로 term 쿼리 사용)
        if (skills != null && !skills.isEmpty()) {
            log.info("✅ skills 필터 추가: {}", skills);
            for (String skill : skills) {
                filterQueries.add(Query.of(q -> q
                        .term(t -> t
                                .field("skills")
                                .value(skill)
                        )
                ));
            }
        }

        // 5. companyName 필터
        if (companyName != null && !companyName.trim().isEmpty()) {
            log.info("✅ companyName 필터 추가: {}", companyName);
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("careers")
                            .query(nq -> nq
                                    .match(m -> m
                                            .field("careers.companyName")
                                            .query(companyName)
                                    )
                            )
                    )
            ));
        }

        // 6. certificateName 필터
        if (certificateName != null && !certificateName.trim().isEmpty()) {
            log.info("✅ certificateName 필터 추가: {}", certificateName);
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("certificates")
                            .query(nq -> nq
                                    .match(m -> m
                                            .field("certificates.name")
                                            .query(certificateName)
                                    )
                            )
                    )
            ));
        }

        // 7. country 필터
        if (country != null && !country.trim().isEmpty()) {
            log.info("✅ country 필터 추가: {}", country);
            filterQueries.add(Query.of(q -> q
                    .nested(n -> n
                            .path("overseasExperiences")
                            .query(nq -> nq
                                    .match(m -> m
                                            .field("overseasExperiences.country")
                                            .query(country)
                                    )
                            )
                    )
            ));
        }

        // BoolQuery 생성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder()
                .must(mustQueries);

        if (!filterQueries.isEmpty()) {
            boolQueryBuilder.filter(filterQueries);
        }

        Query finalQuery = Query.of(q -> q.bool(boolQueryBuilder.build()));

        log.info("📊 최종 Elasticsearch 쿼리: {}", finalQuery.toString());

        // NativeQuery 생성
        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(finalQuery)
                .withPageable(pageable)
                .build();

        // 검색 실행
        SearchHits<ResumeDocument> searchHits = elasticsearchOperations.search(
                searchQuery,
                ResumeDocument.class
        );

        log.info("✅ 검색 결과: {}건", searchHits.getTotalHits());

        List<ResumeDocument> documents = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(documents, pageable, searchHits.getTotalHits());
    }

    // ✅ PageResponse 변환 헬퍼 메서드 (String을 OffsetDateTime으로 파싱)
    private ResumeSearchDto.PageResponse convertToPageResponse(Page<ResumeDocument> page) {
        List<ResumeSearchDto.SearchResponse> content = page.getContent().stream()
                .map(doc -> {
                    // String을 OffsetDateTime으로 변환
                    OffsetDateTime appliedAtOffset = parseToOffsetDateTime(doc.getAppliedAt());

                    return ResumeSearchDto.SearchResponse.builder()
                            .id(Long.valueOf(doc.getId()))
                            .appliedAt(appliedAtOffset)
                            .description(doc.getDescription())
                            .userName(doc.getUserName())
                            .userEmail(doc.getUserEmail())
                            .userPhone(doc.getUserPhone())
                            .jobPostingId(doc.getJobPostingId())
                            .jobPostingTitle(doc.getJobPostingTitle())
                            .careers(convertCareers(doc.getCareers()))
                            .educations(convertEducations(doc.getEducations()))
                            .skills(doc.getSkills())
                            .certificateCount(doc.getCertificates() != null ? doc.getCertificates().size() : 0)
                            .build();
                })
                .collect(Collectors.toList());

        return ResumeSearchDto.PageResponse.builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private List<ResumeSearchDto.CareerSummary> convertCareers(List<ResumeDocument.CareerInfo> careers) {
        if (careers == null) return new ArrayList<>();

        return careers.stream()
                .map(c -> {
                    // String을 OffsetDateTime으로 변환
                    OffsetDateTime startDateOffset = parseToOffsetDateTime(c.getStartDate());
                    OffsetDateTime endDateOffset = parseToOffsetDateTime(c.getEndDate());

                    return ResumeSearchDto.CareerSummary.builder()
                            .companyName(c.getCompanyName())
                            .position(c.getPosition())
                            .startDate(startDateOffset)
                            .endDate(endDateOffset)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<ResumeSearchDto.EducationSummary> convertEducations(List<ResumeDocument.EducationInfo> educations) {
        if (educations == null) return new ArrayList<>();

        return educations.stream()
                .map(e -> ResumeSearchDto.EducationSummary.builder()
                        .schoolName(e.getSchoolName())
                        .major(e.getMajor())
                        .degree(e.getDegree())
                        .build())
                .collect(Collectors.toList());
    }

    // ✅ String을 OffsetDateTime으로 파싱하는 헬퍼 메서드
    private OffsetDateTime parseToOffsetDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            // ISO 8601 형식 파싱 시도 (2023-07-22T00:00:00)
            if (dateStr.contains("T")) {
                java.time.LocalDateTime localDateTime = java.time.LocalDateTime.parse(dateStr);
                return localDateTime.atOffset(ZoneOffset.UTC);
            } else {
                // 날짜만 있는 경우 (2023-07-22)
                java.time.LocalDate localDate = java.time.LocalDate.parse(dateStr);
                return localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
            }
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateStr, e);
            return null;
        }
    }
}