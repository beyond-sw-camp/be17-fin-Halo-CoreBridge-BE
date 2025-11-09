package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
}
