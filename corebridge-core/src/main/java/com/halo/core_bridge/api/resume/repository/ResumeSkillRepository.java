package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.entity.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Long> {
}
