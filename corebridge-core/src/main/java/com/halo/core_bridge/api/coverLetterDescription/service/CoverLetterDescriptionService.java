package com.halo.core_bridge.api.coverLetterDescription.service;

import com.halo.core_bridge.api.coverLetterDescription.model.dto.CoverLetterDescriptionDto;
import com.halo.core_bridge.api.coverLetterDescription.model.entity.CoverLetterDescription;
import com.halo.core_bridge.api.coverLetterDescription.repository.CoverLetterDescriptionRepository;
import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import com.halo.core_bridge.api.coverLetterTitle.service.CoverLetterTitleService;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoverLetterDescriptionService {

    private final CoverLetterDescriptionRepository repository;
    private final CoverLetterTitleService coverLetterTitleService;
    private final ResumeService resumeService;

    @Transactional
    public Long create(CoverLetterDescriptionDto.CoverLetterDescriptionRequest dto) {
        Resume resume = resumeService.findById(dto.getResumeId());
        CoverLetterTitle coverLetterTitle = coverLetterTitleService.findById(dto.getCoverLetterTitleId());

        CoverLetterDescription coverLetterDescription = dto.toEntity(coverLetterTitle, resume);
        CoverLetterDescription savedDescription = repository.save(coverLetterDescription);

        return savedDescription.getId();
    }

    public List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse> list(Long resumeId, Long jobPostId) {
        List<CoverLetterDescription> descriptions = repository.findAllByResumeIdAndJobPostingId(resumeId, jobPostId);
        return descriptions.stream()
                .map(CoverLetterDescriptionDto.CoverLetterDescriptionResponse::from)
                .collect(Collectors.toList());
    }
}