package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.coverLetterTitle.model.dto.CoverLetterTitleDto;
import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import com.halo.core_bridge.api.coverLetterTitle.repository.CoverLetterTitleRepository;
import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.model.entity.TechStack;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import com.halo.core_bridge.api.jobposting.repository.JobPostingSkillRepository;
import com.halo.core_bridge.api.jobposting.repository.JobPostingsQueryRepository;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.*;
import static com.halo.core_bridge.common.model.BaseResponseStatus.DELETE_NOT_ALLOWED_DURING_APPLICATION;
import static com.halo.core_bridge.common.model.BaseResponseStatus.JOB_POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingSkillRepository jobPostingSkillRepository;
    private final RecruitProcessRepository recruitProcessRepository;
    private final CoverLetterTitleRepository  coverLetterTitleRepository;
    private final JobPostingsQueryRepository jobPostingsQueryRepository;
    private final ResumeRepository resumeRepository;


    // 채용공고 등록
    @Transactional
    public Long save(CreateRequest dto, Long UserId) {

        JobPosting jobPosting = jobPostingRepository.save(dto.toEntity(UserId));
        return jobPosting.getId();
    }

    // 채용공고 리스트 조회
    @Transactional(readOnly = true)
    public List<JobPostingListResponseDto> getJobPostingList() {
        /**
         * 채용공고 리스트 조회 (성능 개선 버전)
         * - 채용공고, 부서, 단계별 지원자 수를 한 번에 조회
         * - N+1 문제 완전 제거
         */
        return jobPostingRepository.findAllJobPostingSummaries();
    }

    // 상세조회
    @Transactional(readOnly = true)
    public JobPostingDto.DetailResponse getDetail(Long jobPostingId) {
        return jobPostingRepository.findJobPostingDetail(jobPostingId);
    }

    //편집용 조회
    @Transactional(readOnly = true)
    public EditResponse getEditResponse(Long jobPostingId) {
        JobPosting jp = jobPostingRepository.findById(jobPostingId).orElseThrow(() -> BaseException.from(JOB_POSTING_NOT_FOUND));

        List<CoverLetterTitle> questionnaires = coverLetterTitleRepository.findAllByJobPostingId(jobPostingId);

        List<CoverLetterTitleDto.CoverLetterTitleResponse> resultList = new ArrayList<>();
        for (CoverLetterTitle title : questionnaires) {
            resultList.add(CoverLetterTitleDto.CoverLetterTitleResponse.from(title));
        }

        return EditResponse.fromEntity(jp, resultList);
    }

    //채용공고 헤더(기본정보) 조회요청
    @Transactional(readOnly = true)
    public HeaderResponse getHeaderDetail(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(JOB_POSTING_NOT_FOUND));

        return HeaderResponse.fromEntity(jobPosting);
    }

    @Transactional
    public void updateJobPosting(Long id, UpdateRequest request) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(JOB_POSTING_NOT_FOUND));

        //  1. JobPosting의 기본 필드 업데이트
        request.applyUpdates(jobPosting);

        //  2. 기술 스택 갱신
        if (request.getTechStack() != null) {
            jobPostingSkillRepository.deleteAllByJobPosting(jobPosting);
            List<JobPostingSkill> newSkills = request.getTechStack().stream()
                    .map(skill -> JobPostingSkill.builder()
                            .jobPosting(jobPosting)
                            .name(TechStack.valueOf(skill))
                            .build())
                    .toList();
            jobPostingSkillRepository.saveAll(newSkills);
        }

        //  3. 채용 프로세스 갱신
        if (request.getRecruitProcess() != null) {
            recruitProcessRepository.deleteAllByJobPosting(jobPosting);
            List<RecruitProcess> newProcesses = request.getRecruitProcess().stream()
                    .map(p -> RecruitProcess.builder()
                            .jobPosting(jobPosting)
                            .name(p.getName())
                            .colorCode(p.getColor()) // String 컬럼
                            .orderIdx(p.getOrderIdx())
                            .build())
                    .toList();
            recruitProcessRepository.saveAll(newProcesses);
        }

        //  4. 자기소개서 문항 갱신
        if (request.getCoverLetterTitles() != null) {
            coverLetterTitleRepository.deleteAllByJobPostingId(id);
            List<CoverLetterTitle> newQuestions = request.getCoverLetterTitles().stream()
                    .map(q -> CoverLetterTitle.builder()
                            .jobPostingId(id)
                            .title(q.getTitle())
                            .subtitle(q.getSubtitle())
                            .build())
                    .toList();
            coverLetterTitleRepository.saveAll(newQuestions);
        }

        //  5. 변경사항 저장
        jobPostingRepository.save(jobPosting);
    }

    @Transactional(readOnly = true)
    public JobPosting getById(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> BaseException.from(JOB_POSTING_NOT_FOUND));
    }

    @Transactional
    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id).orElseThrow(() -> BaseException.from(JOB_POSTING_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(jobPosting.getApplyStartDate()) && now.isBefore(jobPosting.getHireEndDate())) {
            throw BaseException.from(DELETE_NOT_ALLOWED_DURING_APPLICATION);
        }
        // 조건 통과 시 삭제
        jobPostingRepository.deleteById(id);
    }

    /**
     * 키워드로 검색 하는 기능
     * @param searchQuery 검색 쿼리 파라미터가 저장된 DTO
     */
    public JobPostingPage searchJobPostings(SearchQuery searchQuery) {

        PageRequest pageable = PageRequest.of(searchQuery.getPage(), 10, Sort.by("id").descending());

        Page<JobPostingQuery> resultPage = jobPostingsQueryRepository.searchJobPostings(searchQuery, pageable);
        List<JobPostingQuery> findJobPostings = resultPage.getContent();

        return JobPostingPage.from(
                toJobPostingResp(findJobPostings),
                resultPage.getNumber(),
                resultPage.getTotalPages(),
                resultPage.getTotalElements()
        );
    }

    private List<JobPostingsResp> toJobPostingResp(List<JobPostingQuery> findJobPostings) {
        return findJobPostings.stream().map(jobPosting ->
                JobPostingsResp.from(
                        jobPosting,
                        computeStatus(jobPosting.getApplyStartDate(), jobPosting.getHireEndDate()),
                        computeDDay(jobPosting.getHireEndDate()),
                        computeProgressByPeriod(jobPosting.getApplyStartDate(), jobPosting.getHireEndDate())
                )
        ).toList();
    }

    // 채용 상태 계산("예정" / "채용중" / "마감")

    /**
     * 접수 시작일과 채용 마감일을 사용하여 채용 상태를 계산한다.
     * @param start 접수 시작일
     * @param end 채용 마감일
     * @return 채용 상태 문자열
     */
    private String computeStatus(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(start)) return "예정";
        if (now.isAfter(end)) return "마감";
        return "채용중";
    }

    /**
     * D-Day를 계산한다
     * @param end 채용 마감일
     * @return D-Day 문자열
     */
    private String computeDDay(LocalDateTime end) {
        long diff = ChronoUnit.DAYS.between(LocalDate.now(), end.toLocalDate());

        if (diff > 0) {
            return "D-" + diff;
        } else if (diff == 0) {
            return "D-Day";
        } else {
            return "마감";
        }
    }

    /**
     * 채용 진행률을 계산한다.
     * @param startDate 채용 시작일
     * @param endDate 채용 마감일
     * @return 채용 진행률
     */
    private Integer computeProgressByPeriod(LocalDateTime startDate, LocalDateTime endDate) {

        LocalDate today = LocalDate.now();
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        long totalDays = ChronoUnit.DAYS.between(start, end);

        long passedDays = ChronoUnit.DAYS.between(start, today);
        double progress = (double) passedDays / totalDays * 100;

        if (progress < 0) return 0;
        if (progress > 100) return 100;

        return (int) Math.round(progress);
    }
}
