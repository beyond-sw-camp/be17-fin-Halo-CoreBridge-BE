package com.halo.core_bridge.api.coverLetterTitle.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoverLetterTitleRepository extends JpaRepository<CoverLetterTitle, Long> {

    List<CoverLetterTitle> findAllByJobPostingId(Long jobPostingId);
}