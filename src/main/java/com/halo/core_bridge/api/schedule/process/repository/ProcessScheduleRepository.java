package com.halo.core_bridge.api.schedule.process.repository;

import com.halo.core_bridge.api.schedule.process.model.entity.ProcessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcessScheduleRepository extends JpaRepository<ProcessSchedule, Long> {
    List<ProcessSchedule> findByJobPostingId(Long jobPostingId);
    void deleteByRecurringGroupId(String recurringGroupId);
}
