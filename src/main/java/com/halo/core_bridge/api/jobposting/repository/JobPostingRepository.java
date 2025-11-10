package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long>, JobPostingQueryRepository {
    @EntityGraph(attributePaths = {"department"})
    @Query("SELECT j FROM JobPosting j ORDER BY j.createdAt DESC")
    List<JobPosting> findAllWithDepartment();
}
