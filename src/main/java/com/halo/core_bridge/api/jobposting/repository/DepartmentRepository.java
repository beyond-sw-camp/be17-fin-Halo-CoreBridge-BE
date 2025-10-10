package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.organization.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
