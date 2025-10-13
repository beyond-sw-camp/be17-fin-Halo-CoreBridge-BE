package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.*;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto.*;
import com.halo.core_bridge.api.resume.model.dto.ResumeResponseDto.*;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {

    private final ResumeRepository resumeRepository;

    @Transactional
    public Long register(Create requestDto, Long userId, Long jobPostingId) {

        Resume resume = requestDto.toEntity(null, null);

        Resume savedResume = resumeRepository.save(resume);
        log.info("새로운 이력서가 등록되었습니다. ID: {}", savedResume.getId());
        return savedResume.getId();
    }

    public Detail read(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.RESUME_NOT_FOUND));
        return Detail.from(resume);
    }

    public Slice<Detail> list(Pageable pageable) {
        Slice<Resume> resumes = resumeRepository.findAll(pageable);

        return resumes.map(Detail::from);
    }

    @Transactional
    public Long update(Long resumeId, Update requestDto) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.RESUME_NOT_FOUND));

        resume.updateDescription(requestDto.getDescription());

        resume.getCareers().clear();
        requestDto.getCareers().stream()
                .map(CareerDto::toEntity)
                .forEach(resume::addCareer);

        resume.getEducations().clear();
        requestDto.getEducations().stream()
                .map(EducationDto::toEntity)
                .forEach(resume::addEducation);

        resume.getCertificates().clear();
        requestDto.getCertificates().stream()
                .map(CertificateDto::toEntity)
                .forEach(resume::addCertificate);

        resume.getLanguages().clear();
        requestDto.getLanguages().stream()
                .map(LanguageDto::toEntity)
                .forEach(resume::addLanguage);

        resume.getOverseasExperiences().clear();
        requestDto.getOverseasExperiences().stream()
                .map(OverseasExperienceDto::toEntity)
                .forEach(resume::addOverseasExperience);

        resume.getResumeSkills().clear();
        requestDto.getResumeSkills().stream()
                .map(ResumeSkillDto::toEntity)
                .forEach(resume::addResumeSkill);

        log.info("이력서 정보가 수정되었습니다. ID: {}", resume.getId());
        return resume.getId();
    }

    @Transactional
    public void deleted(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.RESUME_NOT_FOUND));
        resumeRepository.delete(resume);
        log.info("이력서가 삭제되었습니다. ID: {}", resumeId);
    }
}