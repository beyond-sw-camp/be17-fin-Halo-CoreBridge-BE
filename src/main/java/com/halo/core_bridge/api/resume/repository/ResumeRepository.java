package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByJobPostingIdAndProcessId(Long jobPostingId, Long processId);
}