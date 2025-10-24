package com.halo.core_bridge.api.schedule.jobposting.repository;

import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingScheduleRepository extends JpaRepository<JobPostingSchedule, Long> {
    List<JobPostingSchedule> findByJobPostingId(Long jobPostingId);
    void deleteByRecurringGroupId(String recurringGroupId);
}
