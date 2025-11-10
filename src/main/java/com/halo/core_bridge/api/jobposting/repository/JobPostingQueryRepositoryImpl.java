package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.*;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.organization.model.entity.QDepartment;
import com.halo.core_bridge.api.resume.model.entity.QResume;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
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
}
