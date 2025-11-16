package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.model.dto.InterviewerDto.InterviewerInfo;
import com.halo.core_bridge.api.interview.model.entity.QInterview;
import com.halo.core_bridge.api.interview.model.entity.QInterviewAssignment;
import com.halo.core_bridge.api.interview.model.entity.QInterviewer;
import com.halo.core_bridge.api.jobposting.model.entity.QJobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.QResume;
import com.halo.core_bridge.api.users.model.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InterviewQueryRepositoryImpl implements  InterviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QInterview interview = QInterview.interview;
    private final QRecruitProcess recruitProcess = QRecruitProcess.recruitProcess;
    private final QInterviewer interviewer = QInterviewer.interviewer;
    private final QJobPosting jobPosting = QJobPosting.jobPosting;
    private final QResume resume = QResume.resume;
    private final QUser user = QUser.user;
    private final QInterviewAssignment interviewAssignment = QInterviewAssignment.interviewAssignment;

    @Override
    public Page<InterviewDto.Read> search(InterviewDto.SearchQuery searchQuery, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();

        // 검색 조건
        if (hasText(searchQuery.getKeyword())) {
            condition.and(interview.resume.user.name.containsIgnoreCase(searchQuery.getKeyword()));
        }

        if (searchQuery.getStatus() != null) {
            condition.and(interview.status.eq(searchQuery.getStatus()));
        }

        List<InterviewDto.Read> read = jpaQueryFactory
                .select(
                        Projections.fields(
                                InterviewDto.Read.class,
                                interview.id.as("id"),
                                interview.startDateTime.as("startDateTime"),
                                interview.duration.as("duration"),
                                interview.location.as("location"),
                                interview.interviewType.as("interviewType"),
                                interview.status.as("interviewStatus"),
                                interview.description.as("description"),
                                user.name.as("name"),
                                jobPosting.id.as("jobPostingId"),
                                recruitProcess.name.as("process"),
                                jobPosting.id.as("jobPostingId")
                        )
                )
                .from(interview)
                .leftJoin(interview.resume, resume)
                .leftJoin(resume.user, user)
                .leftJoin(resume.jobPosting, jobPosting)
                .leftJoin(interview.recruitProcess, recruitProcess)
                .where(condition)
                .orderBy(interview.startDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> jobPostingIds = read.stream().map(InterviewDto.Read::getJobPostingId).toList();

        List<InterviewerInfo> interviewers  = jpaQueryFactory
                .select(
                        Projections.fields(
                                InterviewerInfo.class,
                                interviewer.id.as("id"),
                                user.name.as("name"),
                                user.email.as("email"),
                                interviewer.jobPosting.id.as("jobPostingId")
                        )
                )
                .from(interviewer)
                .leftJoin(interviewer.user, user)
                .where(interviewer.jobPosting.id.in(jobPostingIds))
                .fetch();


        Map<Long, List<InterviewerInfo>> interviewersByJobPostingId = interviewers.stream().collect(
                Collectors.groupingBy(InterviewerInfo::getJobPostingId)
        );

        read.forEach(r -> r.setInterviewers(interviewersByJobPostingId.getOrDefault(r.getJobPostingId(), List.of())));

        Long total = jpaQueryFactory
                .select(interview.count())
                .from(interview)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(read, pageable, total != null ? total : 0);
    }

    public Page<InterviewDto.Read> searchV2(InterviewDto.SearchQuery searchQuery, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();

        // 검색 조건
        if (hasText(searchQuery.getKeyword())) {
            condition.and(interview.resume.user.name.containsIgnoreCase(searchQuery.getKeyword()));
        }

        if (searchQuery.getStatus() != null) {
            condition.and(interview.status.eq(searchQuery.getStatus()));
        }

        List<InterviewDto.Read> findInterview = jpaQueryFactory
                .select(
                        Projections.fields(
                                InterviewDto.Read.class,
                                interview.id.as("id"),
                                interview.startDateTime.as("startDateTime"),
                                interview.duration.as("duration"),
                                interview.location.as("location"),
                                interview.interviewType.as("interviewType"),
                                interview.status.as("interviewStatus"),
                                interview.description.as("description"),
                                user.name.as("name"),
                                recruitProcess.name.as("process")
                        )
                )
                .from(interviewAssignment)
                .leftJoin(interviewAssignment.interview, interview)
                .leftJoin(interview.resume, resume)
                .leftJoin(resume.user, user)
                .leftJoin(interview.recruitProcess, recruitProcess)
                .where(condition)
                .where(interviewAssignment.interviewer.id.eq(searchQuery.getUserId()))
                .orderBy(interview.startDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 면접관들 목록을 찾아야한다. 면접관들은 InterviewAssginment에서 인터뷰 id로 찾을 수 있다.

        List<InterviewerInfo> interviewerInfoList = jpaQueryFactory.select(
                        Projections.fields(
                                InterviewerInfo.class,
                                interviewAssignment.id.as("id"),
                                user.name.as("name"),
                                user.email.as("email"),
                                interview.id.as("interviewId")
                        ))
                .from(interviewAssignment)
                .leftJoin(interviewAssignment.interview, interview)
                .leftJoin(interviewAssignment.interviewer, user)
                .fetch();

        Map<Long, List<InterviewerInfo>> interviewerByInterviewId = interviewerInfoList.stream().collect(
                Collectors.groupingBy(InterviewerInfo::getInterviewId)
        );

        findInterview.forEach(r -> r.setInterviewers(
                interviewerByInterviewId.getOrDefault(r.getId(), List.of()))
        );

        Long total = jpaQueryFactory
                .select(interview.count())
                .from(interviewAssignment)
                .join(interviewAssignment.interview, interview)
                .join(interviewAssignment.interviewer, user)
                .where(condition)
                .where(user.id.eq(searchQuery.getUserId()))
                .fetchOne();

        return new PageImpl<>(findInterview, pageable, total != null ? total : 0);
    }

    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }
}
