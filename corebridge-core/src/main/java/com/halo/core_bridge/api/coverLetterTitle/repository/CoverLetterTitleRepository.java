package com.halo.core_bridge.api.coverLetterTitle.repository;

import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverLetterTitleRepository extends JpaRepository<CoverLetterTitle, Long> {

    List<CoverLetterTitle> findAllByJobPostingId(Long jobPostingId);

    void deleteAllByJobPostingId(Long jobPostingId);
}