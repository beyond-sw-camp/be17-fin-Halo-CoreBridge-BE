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
    private final JobPostingSkillService jobPostingSkillService;

    // 채용공고 등록
    @Transactional
    public Long save(JobPostingDto.CreateRequest dto, Long UserId) {

        JobPosting jobPosting = jobPostingRepository.save(dto.toEntity(UserId));

        return jobPosting.getId();
    }

    // 전체 목록 조회
    public List<JobPostingDto.ListResponse> getList() {
        return null;
    }

    // 상세조회
    @Transactional(readOnly = true)
    public JobPostingDto.DetailResponse getDetail(Long id) {
        return null;
    }

    @Transactional
    public void updateJobPosting(Long id, JobPostingDto.UpdateRequest dto) {

    }

    @Transactional
    public void deleteJobPosting(Long id) {

    }
}
