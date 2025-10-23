package com.halo.core_bridge.api.pdf.repository;

import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdfRepository extends JpaRepository<Pdf, Long> {
    Optional<Pdf> findByResumeId(Long resumeId);
    Optional<Pdf> findByResumeIdAndIsDeletedFalse(Long resumeId);
    Optional<Pdf> findByIdAndIsDeletedFalse(Long id);
}
