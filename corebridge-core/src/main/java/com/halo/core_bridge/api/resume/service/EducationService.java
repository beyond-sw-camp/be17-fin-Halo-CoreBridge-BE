package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.EducationDto;
import com.halo.core_bridge.api.resume.model.entity.Education;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.EducationRepository;
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
public class EducationService {
    private final EducationRepository educationRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Long create(EducationDto dto, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Education education = dto.toEntity(resume);
        Education saved = educationRepository.save(education);

        log.info("Education created: {}", saved.getId());
        return saved.getId();
    }

    public EducationDto read(Long id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        return EducationDto.from(education);
    }

    @Transactional
    public void update(Long id, EducationDto dto) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        // 기존 education 삭제 후 새로운 education 생성
        Resume resume = education.getResume();
        educationRepository.delete(education);

        Education newEducation = dto.toEntity(resume);
        educationRepository.save(newEducation);

        log.info("Education updated: {}", id);
    }

    public List<EducationDto> list(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume.getEducations().stream()
                .map(EducationDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        educationRepository.deleteById(id);
        log.info("Education deleted: {}", id);
    }
}
