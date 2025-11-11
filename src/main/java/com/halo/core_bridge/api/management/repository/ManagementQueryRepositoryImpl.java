package com.halo.core_bridge.api.management.repository;

import com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess;
import com.halo.core_bridge.api.management.model.dto.ManagementDto;
import com.halo.core_bridge.api.resume.model.entity.QCareer;
import com.halo.core_bridge.api.resume.model.entity.QResume;
import com.halo.core_bridge.api.users.model.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ManagementQueryRepositoryImpl implements ManagementQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ManagementDto findManagementByJobPostingId(Long jobPostingId) {

        // ===============================
        // Q-Class 초기화
        // ===============================
        QRecruitProcess process = QRecruitProcess.recruitProcess;
        QResume resume = QResume.resume;
        QCareer career = QCareer.career;
        QUser user = QUser.user;


        // ===============================
        // OIN으로 필요한 데이터 조회
        // -------------------------------
        // recruit_process 기준으로 resume, user, career 조인
        // career가 여러 개일 수 있으므로 flat 결과로 받음
        // ===============================
        List<Tuple> tuples = queryFactory
                .select(
                        process.id,
                        process.name,
                        process.colorCode,
                        resume.id,
                        resume.applied_at,
                        user.name,
                        career.startDate,
                        career.endDate
                )
                .from(process)
                .leftJoin(resume).on(resume.process.eq(process))
                .leftJoin(resume.user, user)
                .leftJoin(resume.careers, career)
                .where(process.jobPosting.id.eq(jobPostingId))
                .orderBy(process.orderIdx.asc())
                .fetch();


        // ===============================
        //  이력서별 총 근속연수 계산
        // -------------------------------
        // 동일한 resume.id 에 대해 career 여러 개일 수 있으므로
        // Stream groupingBy 로 근속연수 합산
        // ===============================
        Map<Long, Double> experienceMap = tuples.stream()
                .filter(tuple -> tuple.get(resume.id) != null && tuple.get(career.startDate) != null)
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(resume.id),
                        Collectors.summingDouble(tuple -> {
                            LocalDate start = tuple.get(career.startDate).toLocalDate();
                            LocalDate end = Optional.ofNullable(tuple.get(career.endDate))
                                    .map(LocalDateTime::toLocalDate)
                                    .orElse(LocalDate.now());
                            return ChronoUnit.DAYS.between(start, end) / 365.0;
                        })
                ));


        // ===============================
        // Stage별 DTO 그룹핑 + 중복 제거
        // -------------------------------
        // Stage별(RecruitProcess)로 applicants를 누적하되,
        // 같은 resumeId 가 career 여러 개일 경우 중복 방지
        // ===============================
        Map<Long, ManagementDto.StageDto> stageMap = new LinkedHashMap<>();

        // Stage별 중복 방지를 위해 각 Stage마다 별도의 Set 사용
        Map<Long, Set<Long>> stageResumeSet = new HashMap<>();

        for (Tuple tuple : tuples) {
            Long processId = tuple.get(process.id);
            String processName = tuple.get(process.name);
            String colorCode = tuple.get(process.colorCode).toString();

            // StageDto 없으면 새로 추가
            stageMap.computeIfAbsent(processId, id ->
                    ManagementDto.StageDto.builder()
                            .id(processId)
                            .name(processName)
                            .colorCode(colorCode)
                            .applicants(new ArrayList<>())
                            .build()
            );

            // 각 Stage별 resumeId 중복체크용 Set 초기화
            stageResumeSet.computeIfAbsent(processId, id -> new HashSet<>());

            Long resumeId = tuple.get(resume.id);
            if (resumeId == null) continue;

            // 같은 Stage 안에서 동일한 지원자가 여러 Career로 중복 조회되는 경우 제거
            if (stageResumeSet.get(processId).contains(resumeId)) continue;
            stageResumeSet.get(processId).add(resumeId);

            // ===============================
            // ApplicantDto 매핑
            // ===============================
            String userName = tuple.get(user.name);
            LocalDateTime appliedAt = tuple.get(resume.applied_at);
            long daysSinceApplied = ChronoUnit.DAYS.between(
                    appliedAt != null ? appliedAt : LocalDateTime.now(),
                    LocalDateTime.now()
            );
            double experience = Math.round(experienceMap.getOrDefault(resumeId, 0.0) * 10) / 10.0;

            ManagementDto.ApplicantDto applicantDto = ManagementDto.ApplicantDto.builder()
                    .id(resumeId)
                    .name(userName)
                    .experience(experience)
                    .daysSinceApplied(daysSinceApplied)
                    .build();

            stageMap.get(processId).getApplicants().add(applicantDto);
        }


        // ===============================
        //  Stage 리스트 구성 및 DTO 반환
        // ===============================
        List<ManagementDto.StageDto> stages = new ArrayList<>(stageMap.values());

        return ManagementDto.of(jobPostingId, stages);
    }
}
