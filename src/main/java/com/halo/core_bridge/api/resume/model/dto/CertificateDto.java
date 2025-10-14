package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Certificate;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CertificateDto {
    private Long id;
    private String name;
    private String acquiredDate;

    public Certificate toEntity(Resume resume) {
        return Certificate.builder()
                .name(this.name)
                .acquiredDate(this.acquiredDate)
                .resume(resume)
                .build();
    }

    public static CertificateDto from(Certificate certificate) {
        return CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .acquiredDate(certificate.getAcquiredDate())
                .build();
    }
}