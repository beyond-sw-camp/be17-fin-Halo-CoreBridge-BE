package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public RecruitProcessDto.recruitProcesses findAllRecruitProcessesByJobPostingId(Long jobPostingId) {

        List<RecruitProcess> findAllByJobPosting = recruitProcessRepository.findByJobPosting_IdOrderByOrderIdxAsc(jobPostingId);
        return RecruitProcessDto.recruitProcesses.from(findAllByJobPosting);
    }

    /**
     * 채용 프로세스의 순서를 변경한다. <br>
     * <code>fromIdx</code>가 <code>toIdx</code> 값보다 작으면 채용 프로세스는 뒤로 이동한다. <br>
     * 이때, <code>fromIdx</code>와 <code>toIdx</code> 사이의 OrderIndex들은 1씩 감소한다. <br>
     * <br>
     * <code>fromIdx</code>가 <code>toIdx</code> 값보다 크면 채용 프로세스는 앞으로 이동한다. <br>
     * 이때, <code>fromIdx</code>와 <code>toIdx</code> 사이의 OrderIndex들은 1씩 증가한다.
     * @param changeOrder 순서를 변경하는데 필요한 DTO
     */
    @Transactional
    public void changeOrder(RecruitProcessDto.ChangeOrder changeOrder) {
        adjustOrderIndexes(changeOrder.getFromIdx(), changeOrder.getToIdx(), changeOrder.getJobPostingId());
        recruitProcessRepository.updateOrderIdx(changeOrder.getProcessId(), changeOrder.getToIdx());
    }

    private void adjustOrderIndexes(int fromIdx, int toIdx, Long joPostingId) {

        // 프로세스가 뒤로 이동, 사이에 있는 인덱스들은 1씩 감소
        if (toIdx < fromIdx) {
            recruitProcessRepository.decrementOrderIdxes(joPostingId, fromIdx, toIdx);
        } else if (toIdx > fromIdx) {
            recruitProcessRepository.incrementOrderIdxes(joPostingId, fromIdx, toIdx);
        }
    }
}
