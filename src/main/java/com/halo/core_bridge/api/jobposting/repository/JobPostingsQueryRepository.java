package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPosting;
import com.halo.core_bridge.api.organization.model.entity.QDepartment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.SearchQuery;

@Repository
@RequiredArgsConstructor
public class JobPostingsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QJobPosting jobPosting = QJobPosting.jobPosting;
    private final QDepartment department = QDepartment.department;


    public Page<JobPosting> searchJobPostings(SearchQuery keyword, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();

        // 검색 조건
        if (hasText(keyword.getKeyword())) {
            condition.and(jobPosting.title.containsIgnoreCase(keyword.getKeyword()));
        }

        List<JobPosting> results = jpaQueryFactory
                .selectFrom(jobPosting)
                .join(jobPosting.department, department).fetchJoin()
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = jpaQueryFactory
                .select(jobPosting.count())
                .from(jobPosting)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);

    }

    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }
}
