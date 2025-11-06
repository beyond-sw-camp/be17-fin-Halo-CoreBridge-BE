package com.halo.core_bridge.api.schedule.jobposting.repository;

import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingScheduleRepository extends JpaRepository<JobPostingSchedule, Long> {}
