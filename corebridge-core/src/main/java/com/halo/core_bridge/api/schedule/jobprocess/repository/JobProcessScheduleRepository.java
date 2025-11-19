package com.halo.core_bridge.api.schedule.jobprocess.repository;

import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobProcessScheduleRepository extends JpaRepository<JobProcessSchedule, Long> {

    List<JobProcessSchedule> findByJobPostingId(Long jobPostingId);

    Optional<JobProcessSchedule> findByIdAndJobPostingId(Long id, Long jobPostingId);

    List<JobProcessSchedule> findByParentScheduleId(Long parentScheduleId);
}
