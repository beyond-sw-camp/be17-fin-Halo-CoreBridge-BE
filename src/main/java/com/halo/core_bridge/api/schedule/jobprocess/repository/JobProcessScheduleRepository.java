package com.halo.core_bridge.api.schedule.jobprocess.repository;

import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobProcessScheduleRepository extends JpaRepository<JobProcessSchedule, Long> {

    /** 특정 공고의 스케줄 목록 */
    List<JobProcessSchedule> findByJobPostingId(Long jobPostingId);

    /** 특정 공고에 속한 스케줄 단건 조회 */
    Optional<JobProcessSchedule> findByIdAndJobPostingId(Long id, Long jobPostingId);

    /** 특정 공고의 스케줄 삭제 */
    void deleteByIdAndJobPostingId(Long id, Long jobPostingId);
}
