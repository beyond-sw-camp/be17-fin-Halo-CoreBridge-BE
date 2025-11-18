package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.jobposting.model.entity.QJobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto.ApplicantResponse;
import com.halo.core_bridge.api.resume.model.entity.QCertificate;
import com.halo.core_bridge.api.resume.model.entity.QEducation;
import com.halo.core_bridge.api.resume.model.entity.QResume;
import com.halo.core_bridge.api.resume.model.entity.QResumeSkill;
import com.halo.core_bridge.api.users.model.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ApplicantQueryRepositoryImpl implements ApplicantQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ApplicantResponse> findApplicantsByJobPostingId(Long jobPostingId) {
        QResume resume = QResume.resume;
        QUser user = QUser.user;
        QJobPosting jobPosting = QJobPosting.jobPosting;
        QRecruitProcess process = QRecruitProcess.recruitProcess;
        QEducation education = QEducation.education;
        QCertificate certificate = QCertificate.certificate;
        QResumeSkill resumeSkill = QResumeSkill.resumeSkill;


        // 1️⃣ 단일 쿼리
        List<Tuple> tuples = queryFactory
                .select(
                        resume.id,
                        user.name,
                        user.email,
                        jobPosting.careerType,
                        process.name,
                        resume.applied_at,
                        education.degree.max(),
                        certificate.id.countDistinct(),
                        resumeSkill.name
                )
                .from(resume)
                .leftJoin(resume.user, user)
                .leftJoin(resume.jobPosting, jobPosting)
                .leftJoin(resume.process, process)
                .leftJoin(resume.educations, education)
                .leftJoin(resume.certificates, certificate)
                .leftJoin(resume.resumeSkills, resumeSkill)
                .where(jobPosting.id.eq(jobPostingId))
                .groupBy(resume.id, resumeSkill.name)
                .orderBy(resume.applied_at.desc())
                .fetch();

        // 2️⃣ 지원자별 그룹핑
        Map<Long, List<Tuple>> grouped = tuples.stream()
                .collect(Collectors.groupingBy(t -> t.get(resume.id)));

        // 3️⃣ DTO 매핑
        return grouped.entrySet().stream()
                .map(entry -> {
                    Tuple first = entry.getValue().get(0);
                    return ApplicantResponse.builder()
                            .id(first.get(resume.id))
                            .name(first.get(user.name))
                            .email(first.get(user.email))
                            .careerType(first.get(jobPosting.careerType))
                            .skills(entry.getValue().stream()
                                    .map(t -> t.get(resumeSkill.name))
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .toList())
                            .degree(first.get(education.degree.max()))
                            .certificateCount(first.get(certificate.id.countDistinct()).intValue())
                            .applyDate(first.get(resume.applied_at))
                            .stageName(first.get(process.name))
                            .build();
                })
                .toList();
    }
}
