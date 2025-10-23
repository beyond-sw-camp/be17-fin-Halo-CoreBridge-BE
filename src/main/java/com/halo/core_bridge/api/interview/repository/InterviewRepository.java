package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import org.springframework.data.repository.CrudRepository;

public interface InterviewRepository extends CrudRepository<Interview, Long> {
}
