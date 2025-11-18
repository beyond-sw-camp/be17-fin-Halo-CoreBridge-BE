package com.halo.core_bridge.api.coverLetterTitle.service;

import com.halo.core_bridge.api.coverLetterTitle.model.dto.CoverLetterTitleDto;
import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import com.halo.core_bridge.api.coverLetterTitle.repository.CoverLetterTitleRepository;
import com.halo.core_bridge.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.halo.core_bridge.common.model.BaseResponseStatus.FAILD_FOUND_COVERLETTER_TITLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoverLetterTitleService {

    private final CoverLetterTitleRepository coverLetterTitleRepository;

    @Transactional
    public List<Long> create(List<CoverLetterTitleDto.CoverLetterTitleRequest> requestList, Long jobPostingId) {
        List<CoverLetterTitle> titlesToSave = requestList.stream()
                .map(request -> request.toEntity(jobPostingId))
                .collect(Collectors.toList());

        return coverLetterTitleRepository.saveAll(titlesToSave).stream()
                .map(CoverLetterTitle::getId)
                .collect(Collectors.toList());
    }

    public List<CoverLetterTitleDto.CoverLetterTitleResponse> list(Long jobPostingId) {
        List<CoverLetterTitle> titles = coverLetterTitleRepository.findAllByJobPostingId(jobPostingId);
        return titles.stream()
                .map(CoverLetterTitleDto.CoverLetterTitleResponse::from)
                .collect(Collectors.toList());
    }

    public CoverLetterTitle findById(Long id) {
        return coverLetterTitleRepository.findById(id)
                .orElseThrow(() -> BaseException.from(FAILD_FOUND_COVERLETTER_TITLE));    }
}