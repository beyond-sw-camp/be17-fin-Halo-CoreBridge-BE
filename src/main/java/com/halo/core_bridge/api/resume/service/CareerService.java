package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.CareerDto;
import com.halo.core_bridge.api.resume.model.entity.Career;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.CareerRepository;
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
public class CareerService {
    private final CareerRepository careerRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Long create(CareerDto dto, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Career career = dto.toEntity(resume);
        Career saved = careerRepository.save(career);

        log.info("Career created: {}", saved.getId());
        return saved.getId();
    }

    public CareerDto read(Long id) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found"));

        return CareerDto.from(career);
    }

    @Transactional
    public void update(Long id, CareerDto dto) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found"));

        // 기존 career 삭제 후 새로운 career 생성
        Resume resume = career.getResume();
        careerRepository.delete(career);

        Career newCareer = dto.toEntity(resume);
        careerRepository.save(newCareer);

        log.info("Career updated: {}", id);
    }

    public List<CareerDto> list(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume.getCareers().stream()
                .map(CareerDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        careerRepository.deleteById(id);
        log.info("Career deleted: {}", id);
    }
}