package com.halo.core_bridge.api.coverLetterDescription.repository;

import com.halo.core_bridge.api.coverLetterDescription.model.entity.CoverLetterDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoverLetterDescriptionRepository extends JpaRepository<CoverLetterDescription, Long> {
    @Query("SELECT d FROM CoverLetterDescription d " + "JOIN FETCH d.coverLetterTitle t " + "WHERE d.resume.id = :resumeId AND t.jobPostingId = :jobPostingId")
    List<CoverLetterDescription> findAllByResumeIdAndJobPostingId(@Param("resumeId") Long resumeId, @Param("jobPostingId") Long jobPostingId);

    String findByResumeId(Long resumeId);

    @Query("select c.description from CoverLetterDescription c where c.resume.id = :resumeId")
    String findDescriptionByResumeId(Long resumeId);

}
