package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingSkillRepository extends JpaRepository<JobPostingSkill,Long> {
    void deleteAllByJobPosting(JobPosting jobPosting);
}
