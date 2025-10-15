package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.organization.service.DepartmentService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final DepartmentService departmentService;
    private final JobPostingSkillService jobPostingSkillService;

    // 채용공고 등록
    @Transactional
    public Long save(JobPostingDto.CreateRequest dto) {

        Department department = departmentService.getReference(dto.getDepartmentId());
        JobPosting jobPosting = jobPostingRepository.save(dto.toEntity(department));
        jobPostingSkillService.saveAll(jobPosting, dto.getSkills());

        return jobPosting.getId();
    }

    // 전체 목록 조회
    public List<JobPostingDto.ListResponse> getList() {
        List<JobPosting> jobPostings = jobPostingRepository.findAll();
        if (jobPostings.isEmpty()) {
            throw BaseException.from(BaseResponseStatus.JOB_POSTING_EMPTY);
        }
        return jobPostings.stream().map(JobPostingDto.ListResponse::fromEntity).toList();
    }

    // 상세조회
    @Transactional(readOnly = true)
    public JobPostingDto.DetailResponse getDetail(Long id) {
        JobPosting job = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));
        return JobPostingDto.DetailResponse.fromEntity(job);
    }

    @Transactional
    public void updateJobPosting(Long id, JobPostingDto.UpdateRequest dto) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));

        Department department = departmentService.getReference(dto.getDepartmentId());

        jobPosting.update(dto, department);

        if(dto.getSkills() != null) {
            List<JobPostingSkill> newSkills = jobPostingSkillService.saveAll(jobPosting, dto.getSkills());
            jobPosting.updateSkills(newSkills);
        }

    }

    @Transactional
    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));

        jobPostingRepository.delete(jobPosting);
    }
}
