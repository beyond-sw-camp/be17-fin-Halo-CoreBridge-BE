package com.halo.core_bridge.api.management.service;

import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.api.management.model.dto.ManagementDto;
import com.halo.core_bridge.api.management.repository.ManagementQueryRepository;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {
    private final RecruitProcessRepository recruitProcessRepository;
    private final ResumeRepository resumeRepository;
    private final ManagementQueryRepository managementQueryRepository;

    @Transactional(readOnly = true)
    public ManagementDto getManagement(Long jobPostingId) {
        return managementQueryRepository.findManagementByJobPostingId(jobPostingId);
    }

    @Transactional
    public void moveApplicantProcess(Long resumeId, Long processId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(()-> BaseException.from(BaseResponseStatus.RESUME_NOT_FOUND));

        RecruitProcess process = recruitProcessRepository.findById(processId)
                .orElseThrow(()-> BaseException.from(BaseResponseStatus.PROCESS_NOT_FOUND));

        resume.updateProcess(process);
    }
}
