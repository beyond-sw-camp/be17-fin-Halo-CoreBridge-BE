package com.halo.core_bridge.api.management.service;

import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.api.management.model.dto.ManagementDto;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {
    private final RecruitProcessRepository recruitProcessRepository;
    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public ManagementDto getManagement(Long jobPostingId) {
        // 해당 공고의 채용 프로세스 조회
        List<RecruitProcess> processes = recruitProcessRepository.findByJobPosting_IdOrderByOrderIdxAsc(jobPostingId);

        // 각 단계별 지원자 조회
        List<ManagementDto.StageDto> stageDtos = processes.stream()
                .map(process -> {
                    List<Resume> resumes = resumeRepository.findByJobPostingIdAndProcessId(jobPostingId, process.getId());
                    return ManagementDto.StageDto.from(process, resumes);
                })
                .toList();

        return ManagementDto.of(jobPostingId, stageDtos);
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
