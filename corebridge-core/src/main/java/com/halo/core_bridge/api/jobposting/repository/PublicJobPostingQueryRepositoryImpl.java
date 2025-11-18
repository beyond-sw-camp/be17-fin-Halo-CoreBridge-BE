package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPostingSkill;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicJobPostingQueryRepositoryImpl implements PublicJobPostingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PublicJobPostingDto.JobRaw> searchPublicJobs(
            PublicJobPostingDto.PublicJobSearchRequest req,
            Pageable pageable
    ) {
        QJobPosting job = QJobPosting.jobPosting;

        BooleanBuilder where = new BooleanBuilder();

        StringTemplate departmentName = Expressions.stringTemplate(
                "COALESCE({0}, '')",
                job.department.name
        );

        // 🔍 제목 검색
        if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
            where.and(job.title.containsIgnoreCase(req.getKeyword()));
        }

        // 🔍 경력 검색
        if (req.getCareerType() != null) {
            where.and(job.careerType.eq(req.getCareerType()));
        }

        // 🔥 Projection → JobRaw 로 직접 조회
        List<PublicJobPostingDto.JobRaw> results = queryFactory
                .select(Projections.constructor(
                        PublicJobPostingDto.JobRaw.class,
                        job.id,
                        job.title,
                        job.summary,
                        job.careerType,
                        job.location,
                        job.applyEndDate,
                        departmentName
                ))
                .from(job)
                .where(where)
                .orderBy(job.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 🔥 countQuery (필요한 조건만)
        Long total = queryFactory
                .select(job.count())
                .from(job)
                .where(where)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
