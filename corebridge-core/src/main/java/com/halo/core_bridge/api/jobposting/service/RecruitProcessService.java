package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitProcessService {

    private final RecruitProcessRepository recruitProcessRepository;

    @Transactional
    public void create(List<RecruitProcessDto.Create> processList, Long jobPostingId) {
        List<RecruitProcess> recruitProcessList = processList.stream().map(dto -> dto.toEntity(jobPostingId)).toList();
        recruitProcessRepository.saveAll(recruitProcessList);
    }

    /**
     * 새로운 채용 프로세스를 저장한다.
     * @param addRecruitProcess 저장할 채용 프로세스
     * @return <code>Long</code> 새로 저장된 채용 프로세스의 식별자 <code>id</code>를 반환
     */
    @Transactional
    public Long add(RecruitProcessDto.Add addRecruitProcess) {

        // 제일 큰 정렬 번호를 가져온다.
        Integer lastOrderIdx = recruitProcessRepository.findMaxOrderByJobPostingId(addRecruitProcess.getJobPostingId());

        if (lastOrderIdx == null) {
            RecruitProcess savedRecruitProcess = recruitProcessRepository.save(addRecruitProcess.toEntity(1));
            return savedRecruitProcess.getId();
        }

        // 마지막 프로세스의 정렬 번호를 1 증가
        recruitProcessRepository.shiftLastOrderIdx(addRecruitProcess.getJobPostingId(), lastOrderIdx);

        // 새로 추가된 프로세스에 이전의 마지막 프로세스 정렬 번호를 입력
        RecruitProcess savedRecruitProcess = recruitProcessRepository.save(addRecruitProcess.toEntity(lastOrderIdx));

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

    /**
     * 채용 프로세스를 수정한다.
     * @param updateRecruitProcess 수정된 내용이 담겨 있는 수정 DTO
     * @throws BaseException 프로세스가 존재하지 않는 경우 예외 발생
     */
    @Transactional
    public void editRecruitProcess(Long processId, RecruitProcessDto.Update updateRecruitProcess) {

        RecruitProcess findRecruitProcess = recruitProcessRepository.findById(processId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        findRecruitProcess.updateRecruitProcess(updateRecruitProcess.getName(), updateRecruitProcess.getColorCode());
    }

    /**
     * 채용 프로세스를 삭제한다.
     *
     * @param processId 삭제할 채용 프로세스의 ID
     * @return <code>jobPostingId</code> 채용 공고 ID
     */
    @Transactional
    public Long deleteRecruitProcess(Long processId) {

        try {
            RecruitProcess findRecruitProcess = recruitProcessRepository.findById(processId)
                    .orElseThrow(() -> BaseException.from(BaseResponseStatus.RECRUIT_PROCESS_NOT_FOUND));

            Long jobPostingId = findRecruitProcess.getJobPosting().getId();
            int orderIdx = findRecruitProcess.getOrderIdx();

            Integer lastOrderIdx = recruitProcessRepository.findMaxOrderByJobPostingId(jobPostingId);

            if (lastOrderIdx == null)
                throw BaseException.from(BaseResponseStatus.RECRUIT_PROCESS_NOT_FOUND);

            if (lastOrderIdx.equals(orderIdx)) {

                recruitProcessRepository.deleteById(processId);

            } else if (orderIdx < lastOrderIdx) {

                recruitProcessRepository.decrementOrderIdxes(jobPostingId, orderIdx, lastOrderIdx);
                recruitProcessRepository.deleteById(processId);

            }

            recruitProcessRepository.flush();

            return jobPostingId;
        } catch (DataIntegrityViolationException e) {
            throw BaseException.from(BaseResponseStatus.RECRUIT_PROCESS_CANT_DELETE);
        }
    }

    private void adjustOrderIndexes(int fromIdx, int toIdx, Long joPostingId) {

        // 프로세스가 뒤로 이동, 사이에 있는 인덱스들은 1씩 감소
        if (toIdx > fromIdx) {
            recruitProcessRepository.decrementOrderIdxes(joPostingId, fromIdx, toIdx);
        } else if (toIdx < fromIdx) {
            recruitProcessRepository.incrementOrderIdxes(joPostingId, fromIdx, toIdx);
        }
    }
}
