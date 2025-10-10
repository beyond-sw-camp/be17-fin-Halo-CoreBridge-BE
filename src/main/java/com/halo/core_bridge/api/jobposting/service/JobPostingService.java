package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.repository.DepartmentRepository;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final DepartmentRepository departmentRepository;

    // 채용공고 등록
    public JobPostingDto.DetailResponse save(JobPostingDto.CreateRequest dto) {
        Department dept = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.DEPARTMENT_NOT_FOUND));

        var entity = dto.toEntity(dept);
        var saved = jobPostingRepository.save(entity);

        return JobPostingDto.DetailResponse.fromEntity(saved);
    }

    // 전체 목록 조회
    public List<JobPostingDto.ListResponse> getList() {
        List<JobPosting> list = jobPostingRepository.findAll();
        if (list.isEmpty()) {
            throw BaseException.from(BaseResponseStatus.JOB_POSTING_EMPTY);
        }
        return list.stream().map(JobPostingDto.ListResponse::fromEntity).toList();
    }

    // 상세조회
    public JobPostingDto.DetailResponse getDetail(Long id) {
        JobPosting job = jobPostingRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));
        return JobPostingDto.DetailResponse.fromEntity(job);
    }
}
