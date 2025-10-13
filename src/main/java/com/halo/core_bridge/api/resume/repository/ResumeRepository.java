package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}