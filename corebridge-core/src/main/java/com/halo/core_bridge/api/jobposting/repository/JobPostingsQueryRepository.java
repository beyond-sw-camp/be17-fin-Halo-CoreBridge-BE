package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess;
import com.halo.core_bridge.api.organization.model.entity.QDepartment;
import com.halo.core_bridge.api.resume.model.entity.QResume;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.JobPostingQuery;
import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.SearchQuery;

@Repository
@RequiredArgsConstructor
public class JobPostingsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QJobPosting jobPosting = QJobPosting.jobPosting;
    private final QDepartment department = QDepartment.department;
    private final QRecruitProcess recruitProcess = QRecruitProcess.recruitProcess;
    private final QResume resume = QResume.resume;


    public Page<JobPostingQuery> searchJobPostings(SearchQuery keyword, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();

        // 검색 조건
        if (hasText(keyword.getKeyword())) {
            condition.and(jobPosting.title.containsIgnoreCase(keyword.getKeyword()));
        }

        NumberPath<Long> countAlias = Expressions.numberPath(Long.class, "applicantCount");

        List<JobPostingQuery> searchJobPostings = jpaQueryFactory
                .select(
                        Projections.constructor(
                                JobPostingQuery.class,
                                jobPosting.id,
                                jobPosting.title,
                                jobPosting.department.name,
                                jobPosting.employmentType,
                                jobPosting.careerType,
                                jobPosting.hireEndDate,
                                jobPosting.applyStartDate,
                                JPAExpressions.select(resume.count())
                                        .from(resume)
                                        .where(resume.jobPosting.eq(jobPosting)),
                                Expressions.nullExpression(List.class)
                        )
                )
                .from(jobPosting)
                .join(jobPosting.department, department)
                .orderBy(jobPosting.id.desc())
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        List<Long> jobIds = searchJobPostings.stream().map(JobPostingQuery::getId).toList();

        if (jobIds.isEmpty()) {
            return new PageImpl<>(searchJobPostings, pageable, 0);
        }

        List<RecruitProcessDto.ProcessCount> processCounts = jpaQueryFactory
                .select(
                        Projections.constructor(
                                RecruitProcessDto.ProcessCount.class,
                                jobPosting.id,
                                recruitProcess.name,
                                recruitProcess.orderIdx,
                                JPAExpressions.select(resume.count())
                                        .from(resume)
                                        .where(resume.jobPosting.eq(jobPosting))
                        )
                )
                .from(recruitProcess)
                .join(recruitProcess.jobPosting, jobPosting)
                .where(jobPosting.id.in(jobIds))
                .orderBy(recruitProcess.orderIdx.asc())
                .fetch();

        Map<Long, List<RecruitProcessDto.ProcessCount>> processMap = processCounts.stream().collect(
                Collectors.groupingBy(RecruitProcessDto.ProcessCount::getJobPostingId)
        );

        searchJobPostings.forEach(jobPostingQuery -> jobPostingQuery.setProcessSummaries(
                processMap.getOrDefault(jobPostingQuery.getId(), List.of())
                        .stream().map(RecruitProcessDto.ProcessSummary::from)
                        .toList()
                )
        );

        Long total = jpaQueryFactory
                .select(jobPosting.count())
                .from(jobPosting)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(searchJobPostings, pageable, total != null ? total : 0);
    }

    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }
}
