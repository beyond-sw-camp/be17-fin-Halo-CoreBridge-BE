package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    boolean existsByRecruitProcess_IdAndResume_Id(Long recruitProcessId, Long resumeId);

    @Query("""
            select i
            from Interview i
            join fetch i.resume r
            join fetch r.user u
            where i.startDateTime between :from and :to
              and i.reminderSent = false
              and i.status = :status
            """)
    List<Interview> findInterviewsToRemind(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("status") InterviewStatus status);

    @Modifying
    @Query("""
            update Interview i
               set i.reminderSent = true,
                   i.reminderSentAt = :now
             where i.id = :id
            """)
    void markReminderSent(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Query("select u.email from Interview i join i.resume r join r.user u where i.id = :interviewId")
    String findApplicantEmailByInterviewId(Long interviewId);

    @Query("select i from Interview i join fetch i.resume r where i.id = :interviewId")
    Interview findWithResumeByInterviewId(@Param("interviewId") Long interviewId);

    @Query("select i from Interview i where i.status = :interviewStatus and i.startDateTime <= :now")
    List<Interview> findByStatusAndStartDateTimeBefore(InterviewStatus interviewStatus, LocalDateTime now);
}
