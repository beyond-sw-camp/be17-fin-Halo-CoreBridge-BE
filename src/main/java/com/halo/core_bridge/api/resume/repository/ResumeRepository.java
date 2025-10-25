package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByJobPostingIdAndProcessId(Long jobPostingId, Long processId);

    @Query("SELECT COUNT(r) FROM Resume r WHERE r.jobPosting.id = :jobPostingId")
    int countByJobPostingId(Long jobPostingId);

    @Query("SELECT COUNT(r) FROM Resume r WHERE r.process = :recruitProcess")
    int countByRecruitProcess(RecruitProcess recruitProcess);
}