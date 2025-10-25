package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitProcessService {

    private final RecruitProcessRepository recruitProcessRepository;

    @Transactional
    public Long add(RecruitProcessDto.Create createRecruitProcess) {

        RecruitProcess recruitProcessEntity = createRecruitProcess.toEntity();
        RecruitProcess savedRecruitProcess = recruitProcessRepository.save(recruitProcessEntity);

        return savedRecruitProcess.getId();
    }
}
