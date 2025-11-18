package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto;
import com.halo.core_bridge.api.jobposting.model.entity.*;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.organization.model.entity.QDepartment;
import com.halo.core_bridge.api.resume.model.entity.QResume;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("jobPostingQueryRepository")
@RequiredArgsConstructor
public class JobPostingQueryRepositoryImpl implements JobPostingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<JobPostingDto.JobPostingListResponseDto> findAllJobPostingSummaries() {

        // ===============================
        // 0️⃣ Q-Class 초기화
        // ===============================
        QJobPosting jobPosting = QJobPosting.jobPosting;
        QDepartment department = QDepartment.department;
        QRecruitProcess process = QRecruitProcess.recruitProcess;
        QResume resume = QResume.resume;


        // ===============================
        // 1️⃣ 채용 단계별 지원자 수 조회
        // -------------------------------
        // RecruitProcess 기준으로 각 공고(jobPostingId) + 단계별 지원자 수(count)를 가져온다.
        // 결과: List<Tuple> (공고 ID, 단계명, 단계 순서, 해당 단계 지원자 수)
        // ===============================
        List<Tuple> processTuples = queryFactory
                .select(
                        process.jobPosting.id,
                        process.name,
                        process.orderIdx,
                        resume.id.countDistinct()
                )
                .from(process)
                .leftJoin(resume).on(resume.process.eq(process))
                .groupBy(process.jobPosting.id, process.id)
                .fetch();


        // ===============================
        // 2️⃣ Java Stream으로 Map 변환
        // -------------------------------
        // 공고별로 List<ProcessSummary>로 묶어 Map<Long, List<ProcessSummary>> 형태로 만든다.
        // ex) { 1L : [서류 10명, 1차 3명], 2L : [서류 8명, 1차 2명] }
        // ===============================
        Map<Long, List<JobPostingDto.JobPostingListResponseDto.ProcessSummary>> processSummaryMap =
                processTuples.stream()
                        .collect(Collectors.groupingBy(
                                tuple -> tuple.get(process.jobPosting.id),
                                Collectors.mapping(tuple ->
                                                new JobPostingDto.JobPostingListResponseDto.ProcessSummary(
                                                        tuple.get(process.name),
                                                        tuple.get(resume.id.countDistinct()).intValue(),
                                                        tuple.get(process.orderIdx)
                                                ),
                                        Collectors.toList()
                                )
                        ));


        // ===============================
        // 3️⃣ 공고 기본정보 + 총 지원자 수 조회
        // -------------------------------
        // 공고(JobPosting) 기준으로 부서, 근무형태, 경력구분, 지원자수 등을 한 번에 조회한다.
        // groupBy()를 통해 각 공고별로 집계 처리.
        // ===============================
        List<Tuple> jobPostingTuples = queryFactory
                .select(
                        jobPosting.id,
                        jobPosting.title,
                        department.name,
                        jobPosting.employmentType,
                        jobPosting.careerType,
                        jobPosting.applyStartDate,
                        jobPosting.hireEndDate,
                        resume.id.countDistinct()
                )
                .from(jobPosting)
                .leftJoin(jobPosting.department, department)
                .leftJoin(jobPosting.resumes, resume)
                .groupBy(
                        jobPosting.id,
                        jobPosting.title,
                        department.name,
                        jobPosting.employmentType,
                        jobPosting.careerType,
                        jobPosting.applyStartDate,
                        jobPosting.hireEndDate
                )
                .orderBy(jobPosting.createdAt.desc())
                .fetch();


        // ===============================
        // 4️⃣ Tuple → DTO 변환
        // -------------------------------
        // QueryDSL 결과를 DTO로 변환하면서,
        // 각 공고별 processSummaryMap 정보를 함께 주입한다.
        // ===============================
        return jobPostingTuples.stream()
                .map(tuple -> {
                    Long id = tuple.get(jobPosting.id);
                    String title = tuple.get(jobPosting.title);
                    String departmentName = tuple.get(department.name);
                    EmploymentType employmentType = tuple.get(jobPosting.employmentType);
                    CareerType careerType = tuple.get(jobPosting.careerType);
                    LocalDateTime applyStartDate = tuple.get(jobPosting.applyStartDate);
                    LocalDateTime hireEndDate = tuple.get(jobPosting.hireEndDate);
                    Integer applicantCount = tuple.get(resume.id.countDistinct()).intValue();

                    // 공고별 단계별 현황 주입
                    List<JobPostingDto.JobPostingListResponseDto.ProcessSummary> summaries =
                            processSummaryMap.getOrDefault(id, Collections.emptyList());

                    // DTO에서 요구하는 형태에 맞게 임시 JobPosting 객체 생성
                    JobPosting mockJobPosting = JobPosting.builder()
                            .id(id)
                            .title(title)
                            .employmentType(employmentType)
                            .careerType(careerType)
                            .department(Department.builder().name(departmentName).build())
                            .applyStartDate(applyStartDate)
                            .hireEndDate(hireEndDate)
                            .build();

                    // DTO 팩토리 메서드로 변환
                    return JobPostingDto.JobPostingListResponseDto.fromEntity(
                            mockJobPosting,
                            applicantCount,
                            summaries
                    );
                })
                .toList();
    }

    @Override
    public JobPostingDto.DetailResponse findJobPostingDetail(Long jobPostingId) {
        // Q 클래스 초기화
        QJobPosting jobPosting = QJobPosting.jobPosting;
        QDepartment department = QDepartment.department;
        QResume resume = QResume.resume;
        QRecruitProcess process = QRecruitProcess.recruitProcess;
        QJobPostingSkill skill = QJobPostingSkill.jobPostingSkill;

        // ✅ 1️⃣ 공고 + 부서 + 지원자 수 기본 정보
        Tuple base = queryFactory
                .select(
                        jobPosting.id,
                        jobPosting.summary,
                        jobPosting.responsibilities,
                        jobPosting.requirements,
                        jobPosting.preferred,
                        jobPosting.benefits,
                        jobPosting.additionalInfo,
                        jobPosting.createdAt,
                        jobPosting.applyStartDate,
                        jobPosting.applyEndDate,
                        jobPosting.hireEndDate,
                        jobPosting.headcount,
                        jobPosting.workingHours,
                        jobPosting.location,
                        jobPosting.contactName,
                        jobPosting.contactEmail,
                        department.name,
                        resume.id.countDistinct()
                )
                .from(jobPosting)
                .leftJoin(jobPosting.department, department)
                .leftJoin(jobPosting.resumes, resume)
                .where(jobPosting.id.eq(jobPostingId))
                .groupBy(
                        jobPosting.id,
                        jobPosting.summary,
                        jobPosting.responsibilities,
                        jobPosting.requirements,
                        jobPosting.preferred,
                        jobPosting.benefits,
                        jobPosting.additionalInfo,
                        jobPosting.createdAt,
                        jobPosting.applyStartDate,
                        jobPosting.applyEndDate,
                        jobPosting.hireEndDate,
                        jobPosting.headcount,
                        jobPosting.workingHours,
                        jobPosting.location,
                        jobPosting.contactName,
                        jobPosting.contactEmail,
                        department.name
                )
                .fetchOne();

        // ✅ 2️⃣ 프로세스 별도 조회 (ORDER 순서 유지)
        List<RecruitProcessDto.Read> recruitProcesses = queryFactory
                .select(
                        process.id,
                        process.name,
                        process.colorCode,
                        process.orderIdx
                )
                .from(process)
                .where(process.jobPosting.id.eq(jobPostingId))
                .orderBy(process.orderIdx.asc())
                .fetch()
                .stream()
                .map(t -> RecruitProcessDto.Read.builder()
                        .id(t.get(process.id))
                        .name(t.get(process.name))
                        .colorCode(t.get(process.colorCode))
                        .orderIdx(t.get(process.orderIdx))
                        .build())
                .toList();

        // ✅ 3️⃣ 기술스택 별도 조회
        List<TechStack> skills = queryFactory
                .select(skill.name)
                .from(skill)
                .where(skill.jobPosting.id.eq(jobPostingId))
                .distinct()
                .fetch();

        // ✅ 4️⃣ 상태(status) 계산
        String status = computeStatus(base.get(jobPosting.applyStartDate), base.get(jobPosting.hireEndDate));

        // ✅ 5️⃣ 최종 DTO 조립
        JobPostingDto.DetailResponse detailResponse = JobPostingDto.DetailResponse.builder()
                .id(base.get(jobPosting.id))
                .summary(base.get(jobPosting.summary))
                .responsibilities(base.get(jobPosting.responsibilities))
                .requirements(base.get(jobPosting.requirements))
                .preferred(base.get(jobPosting.preferred))
                .benefits(base.get(jobPosting.benefits))
                .additionalInfo(base.get(jobPosting.additionalInfo))
                .status(status)
                .createDate(base.get(jobPosting.createdAt))
                .applyStartDate(base.get(jobPosting.applyStartDate))
                .applyEndDate(base.get(jobPosting.applyEndDate))
                .hireEndDate(base.get(jobPosting.hireEndDate))
                .headCount(base.get(jobPosting.headcount))
                .applicantCount(base.get(resume.id.countDistinct()).intValue())
                .skills(skills)
                .recruitProcesses(recruitProcesses)
                .workingHours(base.get(jobPosting.workingHours))
                .location(base.get(jobPosting.location))
                .contactName(base.get(jobPosting.contactName))
                .contactEmail(base.get(jobPosting.contactEmail))
                .build();

        return detailResponse;
    }

    // ✅ 상태 계산 메서드
    private String computeStatus(LocalDateTime applyStart, LocalDateTime hireEnd) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(applyStart)) return "예정";
        else if (now.isAfter(hireEnd)) return "마감";
        else return "채용중";
    }

    @Override
    public Page<JobPosting> searchJobPostings(JobPostingDto.SearchQuery keyword, Pageable pageable) {

        QJobPosting jobPosting = QJobPosting.jobPosting;
        QDepartment department = QDepartment.department;

        BooleanBuilder condition = new BooleanBuilder();

        // 검색 조건
        if (hasText(keyword.getKeyword())) {
            condition.and(jobPosting.title.containsIgnoreCase(keyword.getKeyword()));
        }

        List<JobPosting> results = queryFactory
                .selectFrom(jobPosting)
                .join(jobPosting.department, department).fetchJoin()
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = queryFactory
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
