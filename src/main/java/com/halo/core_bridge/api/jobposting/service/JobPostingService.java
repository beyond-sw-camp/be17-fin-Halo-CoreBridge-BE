package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import com.halo.core_bridge.api.jobposting.repository.JobPostingSkillRepository;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.organization.service.DepartmentService;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingSkillRepository jobPostingSkillRepository;
    private final ResumeRepository resumeRepository;
    private final RecruitProcessRepository recruitProcessRepository;

    // 채용공고 등록
    @Transactional
    public Long save(JobPostingDto.CreateRequest dto, Long UserId) {

        JobPosting jobPosting = jobPostingRepository.save(dto.toEntity(UserId));

        return jobPosting.getId();
    }

    // 채용공고 리스트 조회
    @Transactional(readOnly = true)
    public List<JobPostingDto.JobPostingListResponseDto> getJobPostingList() {
        // 전체 공고 조회
        List<JobPosting> postings = jobPostingRepository.findAllWithDepartment();

        List<JobPostingDto.JobPostingListResponseDto> resultList = new ArrayList<>();

        for (JobPosting job : postings) {
            int applicantCount = resumeRepository.countByJobPostingId(job.getId());

            // 단계별 현황
            List<RecruitProcess> processes = recruitProcessRepository.findByJobPosting(job);
            List<JobPostingDto.JobPostingListResponseDto.ProcessSummary> processSummaries = new ArrayList<>();

            for (RecruitProcess process : processes) {
                int count = resumeRepository.countByRecruitProcess(process);

                processSummaries.add(
                        JobPostingDto.JobPostingListResponseDto.ProcessSummary.builder()
                                .stageName(process.getName())
                                .orderIndex(process.getOrderIdx())
                                .count(count)
                                .build()
                );
            }

            JobPostingDto.JobPostingListResponseDto dto = JobPostingDto.JobPostingListResponseDto.fromEntity(job, applicantCount, processSummaries);

            resultList.add(dto);
        }

        return resultList;
    }

    // 상세조회
    @Transactional(readOnly = true)
    public JobPostingDto.DetailResponse getDetail(Long id) {
        JobPosting jp = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));

        int applicantCounts = resumeRepository.countByJobPostingId(id);
        return JobPostingDto.DetailResponse.fromEntity(jp, applicantCounts);
    }

    @Transactional
    public void updateJobPosting(Long id, JobPostingDto.UpdateRequest request) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));

        //  기본 필드 변경 감지 후 반영
        if (request.getTitle() != null && !request.getTitle().equals(jobPosting.getTitle())) {
            jobPosting.setTitle(request.getTitle());
        }

        if (request.getEmploymentType() != null) {
            jobPosting.setEmploymentType(request.getEmploymentType());
        }

        if (request.getCareerType() != null) {
            jobPosting.setCareerType(request.getCareerType());
        }

        if (request.getMinExperience() != null) {
            jobPosting.setMinExperience(request.getMinExperience());
        }

        if (request.getMaxExperience() != null) {
            jobPosting.setMaxExperience(request.getMaxExperience());
        }

        if (request.getPositionLevel() != null) {
            jobPosting.setPositionLevel(request.getPositionLevel());
        }

        if (request.getLocation() != null) {
            jobPosting.setLocation(request.getLocation());
        }

        if (request.getApplyStartDate() != null) {
            jobPosting.setApplyStartDate(request.getApplyStartDate());
        }

        if (request.getApplyEndDate() != null) {
            jobPosting.setApplyEndDate(request.getApplyEndDate());
        }

        if (request.getHireEndDate() != null) {
            jobPosting.setHireEndDate(request.getHireEndDate());
        }

        if (request.getHeadcount() != null) {
            jobPosting.setHeadcount(request.getHeadcount());
        }

        if (request.getSummary() != null) {
            jobPosting.setSummary(request.getSummary());
        }

        if (request.getResponsibilities() != null) {
            jobPosting.setResponsibilities(request.getResponsibilities());
        }

        if (request.getRequirements() != null) {
            jobPosting.setRequirements(request.getRequirements());
        }

        if (request.getPreferred() != null) {
            jobPosting.setPreferred(request.getPreferred());
        }

//        // 3기술 스택 업데이트 (있을 경우만)
//        if (request.getTechStack() != null) {
//            jobPostingSkillRepository.deleteAllByJobPosting(jobPosting);
//            for (String techName : request.getTechStack()) {
//                JobPostingSkill skill = JobPostingSkill.builder()
//                        .jobPosting(jobPosting)
//                        .name(techName)
//                        .build();
//                jobPostingSkillRepository.save(skill);
//            }
//        }
//
//        // 채용 프로세스 업데이트(있을 경우만)
//        if(request.getRecruitProcess() != null) {
//            recruitProcessRepository.deleteAllByJobPosting(jobPosting);
//            for(int i = 0; i < request.getRecruitProcess().size(); i++) {
//                RecruitProcess process = RecruitProcess.builder()
//                        .jobPosting(jobPosting)
//                        .name(request.getRecruitProcess().get(i))
//                        .orderIdx(i+1)
//                        .build();
//                recruitProcessRepository.save(process);
//            }
//        }

        //급여정보
        if (request.getSalaryType() != null) jobPosting.setSalaryType(request.getSalaryType());
        if (request.getSalaryMin() != null) jobPosting.setSalaryMin(request.getSalaryMin());
        if (request.getSalaryMax() != null) jobPosting.setSalaryMax(request.getSalaryMax());
        if (request.getSalaryNegotiable() != null) jobPosting.setSalaryNegotiable(request.getSalaryNegotiable());

        // 근무조건
        if (request.getWorkingHours() != null) jobPosting.setWorkingHours(request.getWorkingHours());
        if (request.getBenefits() != null) jobPosting.setBenefits(request.getBenefits());

        // 담당자 정보
        if (request.getContactName() != null) jobPosting.setContactName(request.getContactName());
        if (request.getContactEmail() != null) jobPosting.setContactEmail(request.getContactEmail());

        // 기타 정보
        if (request.getAdditionalInfo() != null) jobPosting.setAdditionalInfo(request.getAdditionalInfo());

        jobPostingRepository.save(jobPosting);
    }

    @Transactional
    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }
}
