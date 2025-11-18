package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long>{
}
