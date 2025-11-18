package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.entity.InterviewAssignment;
import com.halo.core_bridge.api.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewAssignmentRepository extends JpaRepository<InterviewAssignment, Long> {

    List<InterviewAssignment> findByInterviewer(User interviewer);

    Optional<InterviewAssignment> findByInterviewAndInterviewer(Interview interview, User interviewer);
}
