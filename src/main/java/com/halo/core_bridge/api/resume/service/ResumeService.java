package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.pdf.repository.PdfRepository;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
//import com.halo.core_bridge.api.jobposting.service.JobPostingService;
import com.halo.core_bridge.api.users.service.UserService;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.halo.core_bridge.common.model.BaseResponseStatus.RESUME_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {
    private final ResumeRepository resumeRepository;
//    private final JobPostingService jobPostingService;
    private final UserService userService;

    private final CareerService careerService;
    private final CertificateService certificateService;
    private final EducationService educationService;
    private final LanguageService languageService;
    private final OverseasExperienceService overseasExperienceService;
    private final ResumeSkillService resumeSkillService;
    private final PdfRepository pdfRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public Long create(ResumeDto.Create dto, Long userId, MultipartFile file) {
//        JobPosting jobPosting = jobPostingService.read(dto.getJobPostingId());
//        User user = userService.read(userId);

        Resume resume = Resume.builder()
                .applied_at(LocalDateTime.now())  // 서버에서 현재 시각 자동 설정
                .description(dto.getDescription())
//                .jobPosting(jobPosting)
//                .user(user)
                .build();

        Resume saved = resumeRepository.save(resume);
        log.info("Resume created: {}", saved.getId());

        if (dto.getCareers() != null) {
            dto.getCareers().forEach(careerDto ->
                    careerService.create(careerDto, saved.getId())
            );
        }

        if (dto.getCertificates() != null) {
            dto.getCertificates().forEach(certificateDto ->
                    certificateService.create(certificateDto, saved.getId())
            );
        }

        if (dto.getEducations() != null) {
            dto.getEducations().forEach(educationDto ->
                    educationService.create(educationDto, saved.getId())
            );
        }

        if (dto.getLanguages() != null) {
            dto.getLanguages().forEach(languageDto ->
                    languageService.create(languageDto, saved.getId())
            );
        }

        if (dto.getOverseasExperiences() != null) {
            dto.getOverseasExperiences().forEach(overseasDto ->
                    overseasExperienceService.create(overseasDto, saved.getId())
            );
        }

        if (dto.getResumeSkills() != null) {
            dto.getResumeSkills().forEach(skillDto ->
                    resumeSkillService.create(skillDto, saved.getId())
            );
        }

        return saved.getId();
    }



    public ResumeDto.Response read(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return ResumeDto.Response.from(resume);
    }

    @Transactional
    public void update(Long id, ResumeDto.Update dto) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // Resume 기본 정보는 유지하고 자식 엔티티들만 갱신
        // 기존 자식 엔티티들 삭제
        resume.getCareers().forEach(career -> careerService.delete(career.getId()));
        resume.getCertificates().forEach(certificate -> certificateService.delete(certificate.getId()));
        resume.getEducations().forEach(education -> educationService.delete(education.getId()));
        resume.getLanguages().forEach(language -> languageService.delete(language.getId()));
        resume.getOverseasExperiences().forEach(overseas -> overseasExperienceService.delete(overseas.getId()));
        resume.getResumeSkills().forEach(skill -> resumeSkillService.delete(skill.getId()));

        // 새로운 자식 엔티티들 추가
        if (dto.getCareers() != null) {
            dto.getCareers().forEach(careerDto ->
                    careerService.create(careerDto, id)
            );
        }

        if (dto.getCertificates() != null) {
            dto.getCertificates().forEach(certificateDto ->
                    certificateService.create(certificateDto, id)
            );
        }

        if (dto.getEducations() != null) {
            dto.getEducations().forEach(educationDto ->
                    educationService.create(educationDto, id)
            );
        }

        if (dto.getLanguages() != null) {
            dto.getLanguages().forEach(languageDto ->
                    languageService.create(languageDto, id)
            );
        }

        if (dto.getOverseasExperiences() != null) {
            dto.getOverseasExperiences().forEach(overseasDto ->
                    overseasExperienceService.create(overseasDto, id)
            );
        }

        if (dto.getResumeSkills() != null) {
            dto.getResumeSkills().forEach(skillDto ->
                    resumeSkillService.create(skillDto, id)
            );
        }

        log.info("Resume updated: {}", id);
    }

    public List<ResumeDto.Response> list() {
        return resumeRepository.findAll().stream()
                .map(ResumeDto.Response::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        resumeRepository.deleteById(id);
        log.info("Resume deleted: {}", id);
    }

    @Transactional(readOnly = true)
    public Resume getResumeEntity(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> BaseException.from(RESUME_NOT_FOUND));
    }
}
