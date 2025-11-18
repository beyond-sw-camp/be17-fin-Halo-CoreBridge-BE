package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {

    @Query("SELECT i FROM Interviewer i join fetch i.user WHERE i.jobPosting.id = :jobPostingId")
    List<Interviewer> findAllByJobPosting_Id(@Param("jobPostingId") Long jobPostingId);
}
