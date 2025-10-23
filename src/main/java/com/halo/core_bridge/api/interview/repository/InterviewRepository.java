package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
