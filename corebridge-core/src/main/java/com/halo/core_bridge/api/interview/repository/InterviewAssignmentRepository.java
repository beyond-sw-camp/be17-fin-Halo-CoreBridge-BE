package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.entity.InterviewAssignment;
import com.halo.core_bridge.api.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InterviewAssignmentRepository extends JpaRepository<InterviewAssignment, Long> {

    List<InterviewAssignment> findByInterviewer(User interviewer);

    Optional<InterviewAssignment> findByInterviewAndInterviewer(Interview interview, User interviewer);

    Optional<InterviewAssignment> findByInterview_IdAndInterviewer_Id(Long interviewId, Long interviewerId);

    @Query("select i from InterviewAssignment i where i.interview.id = :interviewId")
    List<InterviewAssignment> findByInterview_Id(Long interviewId);
}
