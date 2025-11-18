package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Certificate;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CertificateDto {
    @Schema(description = "자격증 ID", example = "1")
    private Long id;
    @Schema(description = "자격증 이름", example = "정보처리기사")
    private String name;
    @Schema(description = "취득일", example = "2019-05-10")
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