package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.OverseasExperienceDto;
import com.halo.core_bridge.api.resume.model.entity.OverseasExperience;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.OverseasExperienceRepository;
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
public class OverseasExperienceService {
    private final OverseasExperienceRepository overseasExperienceRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Long create(OverseasExperienceDto dto, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        OverseasExperience overseasExperience = dto.toEntity(resume);
        OverseasExperience saved = overseasExperienceRepository.save(overseasExperience);

        log.info("OverseasExperience created: {}", saved.getId());
        return saved.getId();
    }

    public OverseasExperienceDto read(Long id) {
        OverseasExperience overseasExperience = overseasExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OverseasExperience not found"));

        return OverseasExperienceDto.from(overseasExperience);
    }

    @Transactional
    public void update(Long id, OverseasExperienceDto dto) {
        OverseasExperience overseasExperience = overseasExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OverseasExperience not found"));

        // 기존 overseasExperience 삭제 후 새로운 overseasExperience 생성
        Resume resume = overseasExperience.getResume();
        overseasExperienceRepository.delete(overseasExperience);

        OverseasExperience newOverseasExperience = dto.toEntity(resume);
        overseasExperienceRepository.save(newOverseasExperience);

        log.info("OverseasExperience updated: {}", id);
    }

    public List<OverseasExperienceDto> list(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume.getOverseasExperiences().stream()
                .map(OverseasExperienceDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        overseasExperienceRepository.deleteById(id);
        log.info("OverseasExperience deleted: {}", id);
    }
}
