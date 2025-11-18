package com.halo.core_bridge.api.pdf.repository;

import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PdfRepository extends JpaRepository<Pdf, Long> {
    Optional<Pdf> findByResume(Resume resume);
    @Query("SELECT p FROM Pdf p WHERE p.resume.id = :resumeId AND p.isDeleted = false")
    Optional<Pdf> findByResumeIdAndIsDeletedFalse(@Param("resumeId") Long resumeId);
    Optional<Pdf> findByIdAndIsDeletedFalse(Long id);
}
