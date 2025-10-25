package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitProcessRepository extends JpaRepository<RecruitProcess, Long> {

    List<RecruitProcess> findByJobPosting_IdOrderByOrderIdxAsc(Long jobPostingId);

    List<RecruitProcess> findByJobPosting(JobPosting jobPosting);

}
