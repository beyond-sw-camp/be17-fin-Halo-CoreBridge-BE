package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Certificate;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {

    private Long id;
    private String name;
    private String acquiredDate;

    private Resume resume;

    public Certificate toEntity() {
        return Certificate.builder()
                .id(this.id)
                .name(this.name)
                .acquiredDate(this.acquiredDate)
                .resume(this.resume)
                .build();
    }

    public static CertificateDto from(Certificate certificate) {
        return CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .acquiredDate(certificate.getAcquiredDate())
                .resume(certificate.getResume())
                .build();
    }
}