package com.halo.core_bridge.api.schedule.jobprocess.repository;

import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessSchedule;
import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessScheduleShare;
import com.halo.core_bridge.api.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobProcessScheduleShareRepository
        extends JpaRepository<JobProcessScheduleShare, Long> {

    boolean existsByScheduleAndUser(JobProcessSchedule schedule, User user);
}
