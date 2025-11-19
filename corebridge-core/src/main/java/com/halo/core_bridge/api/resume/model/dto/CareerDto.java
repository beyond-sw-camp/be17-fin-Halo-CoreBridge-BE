package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Career;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CareerDto {
    @Schema(description = "경력 ID", example = "1")
    private Long id;
    @Schema(description = "회사명", example = "ABC 주식회사")
    private String companyName;
    @Schema(description = "직책", example = "백엔드 개발자")
    private String position;
    @Schema(description = "시작일", example = "2020-01-01T09:00:00")
    private LocalDateTime startDate;
    @Schema(description = "종료일", example = "2022-12-31T18:00:00")
    private LocalDateTime endDate;

    public Career toEntity(Resume resume) {
        return Career.builder()
                .companyName(this.companyName)
                .position(this.position)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .resume(resume)
                .build();
    }

    public static CareerDto from(Career career) {
        return CareerDto.builder()
                .id(career.getId())
                .companyName(career.getCompanyName())
                .position(career.getPosition())
                .startDate(career.getStartDate())
                .endDate(career.getEndDate())
                .build();
    }
}
