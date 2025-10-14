package com.halo.core_bridge.api.resume.service;

import com.halo.core_bridge.api.resume.model.dto.CertificateDto;
import com.halo.core_bridge.api.resume.model.entity.Certificate;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.CertificateRepository;
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
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Long create(CertificateDto dto, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Certificate certificate = dto.toEntity(resume);
        Certificate saved = certificateRepository.save(certificate);

        log.info("Certificate created: {}", saved.getId());
        return saved.getId();
    }

    public CertificateDto read(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        return CertificateDto.from(certificate);
    }

    @Transactional
    public void update(Long id, CertificateDto dto) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        // 기존 certificate 삭제 후 새로운 certificate 생성
        Resume resume = certificate.getResume();
        certificateRepository.delete(certificate);

        Certificate newCertificate = dto.toEntity(resume);
        certificateRepository.save(newCertificate);

        log.info("Certificate updated: {}", id);
    }

    public List<CertificateDto> list(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume.getCertificates().stream()
                .map(CertificateDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        certificateRepository.deleteById(id);
        log.info("Certificate deleted: {}", id);
    }
}
