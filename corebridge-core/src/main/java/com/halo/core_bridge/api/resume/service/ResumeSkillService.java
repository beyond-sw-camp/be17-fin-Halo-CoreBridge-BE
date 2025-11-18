package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.ResumeSkillDto;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.model.entity.ResumeSkill;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.api.resume.repository.ResumeSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeSkillService {
    private final ResumeSkillRepository resumeSkillRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Long create(ResumeSkillDto dto, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ResumeSkill resumeSkill = dto.toEntity(resume);
        ResumeSkill saved = resumeSkillRepository.save(resumeSkill);

        log.info("ResumeSkill created: {}", saved.getId());
        return saved.getId();
    }

    public ResumeSkillDto read(Long id) {
        ResumeSkill resumeSkill = resumeSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ResumeSkill not found"));

        return ResumeSkillDto.from(resumeSkill);
    }

    @Transactional
    public void update(Long id, ResumeSkillDto dto) {
        ResumeSkill resumeSkill = resumeSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ResumeSkill not found"));

        // 기존 resumeSkill 삭제 후 새로운 resumeSkill 생성
        Resume resume = resumeSkill.getResume();
        resumeSkillRepository.delete(resumeSkill);

        ResumeSkill newResumeSkill = dto.toEntity(resume);
        resumeSkillRepository.save(newResumeSkill);

        log.info("ResumeSkill updated: {}", id);
    }

    public List<ResumeSkillDto> list(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume.getResumeSkills().stream()
                .map(ResumeSkillDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        resumeSkillRepository.deleteById(id);
        log.info("ResumeSkill deleted: {}", id);
    }
}
