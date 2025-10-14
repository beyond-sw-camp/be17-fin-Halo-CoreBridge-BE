package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.LanguageDto;
import com.halo.core_bridge.api.resume.model.entity.Language;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.LanguageRepository;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LanguageService {
    private final LanguageRepository languageRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Long create(LanguageDto dto, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Language language = dto.toEntity(resume);
        Language saved = languageRepository.save(language);

        log.info("Language created: {}", saved.getId());
        return saved.getId();
    }

    public LanguageDto read(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));

        return LanguageDto.from(language);
    }

    @Transactional
    public void update(Long id, LanguageDto dto) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));

        // 기존 language 삭제 후 새로운 language 생성
        Resume resume = language.getResume();
        languageRepository.delete(language);

        Language newLanguage = dto.toEntity(resume);
        languageRepository.save(newLanguage);

        log.info("Language updated: {}", id);
    }

    public List<LanguageDto> list(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume.getLanguages().stream()
                .map(LanguageDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        languageRepository.deleteById(id);
        log.info("Language deleted: {}", id);
    }
}
