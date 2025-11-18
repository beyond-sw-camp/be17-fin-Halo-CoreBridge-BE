package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPostingSkill;
import com.querydsl.core.BooleanBuilder;
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
    public Page<JobPosting> searchPublicJobs(
            PublicJobPostingDto.PublicJobSearchRequest req,
            Pageable pageable
    ) {
        QJobPosting job = QJobPosting.jobPosting;
        QJobPostingSkill skill = QJobPostingSkill.jobPostingSkill;

        BooleanBuilder where = new BooleanBuilder();

        // 🔍 제목 검색
        if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
            where.and(job.title.containsIgnoreCase(req.getKeyword()));
        }

        // 🔍 경력 검색
        if (req.getCareerType() != null) {
            where.and(job.careerType.eq(req.getCareerType()));
        }

        // 🔍 기술스택 검색
        if (req.getTechStacks() != null && !req.getTechStacks().isEmpty()) {
            where.and(skill.name.in(req.getTechStacks()));
        }

        //  결과 조회
        List<JobPosting> results = queryFactory
                .selectDistinct(job)
                .from(job)
                .leftJoin(job.skills, skill)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(job.createdAt.desc())
                .fetch();

        //  count
        Long total = queryFactory
                .select(job.countDistinct())
                .from(job)
                .leftJoin(job.skills, skill)
                .where(where)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
