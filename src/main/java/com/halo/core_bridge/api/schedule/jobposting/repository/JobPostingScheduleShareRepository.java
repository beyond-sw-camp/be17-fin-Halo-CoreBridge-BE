package com.halo.core_bridge.api.schedule.jobposting.repository;

import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingSchedule;
import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingScheduleShare;
import com.halo.core_bridge.api.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingScheduleShareRepository extends JpaRepository<JobPostingScheduleShare, Long> {
    List<JobPostingScheduleShare> findBySchedule(JobPostingSchedule schedule);
    Optional<JobPostingScheduleShare> findByScheduleAndUser(JobPostingSchedule schedule, User user);
    void deleteBySchedule_Id(Long scheduleId);
    boolean existsByScheduleAndUser(JobPostingSchedule schedule, User user);

}
