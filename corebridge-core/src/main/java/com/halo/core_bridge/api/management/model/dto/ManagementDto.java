package com.halo.core_bridge.api.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagementDto {
    private Long jobPostingId;
    private List<StageDto> stages;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StageDto {
        private Long id;
        private String name;
        private String colorCode;
        private List<ApplicantDto> applicants;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicantDto {
        private Long id;
        private String name;
        private double experience;        // DB에서 계산된 경력연수
        private long daysSinceApplied;    // DB 또는 QueryDSL에서 계산된 접수 후 경과일
    }

    public static ManagementDto of(Long jobPostingId, List<StageDto> stages) {
        return ManagementDto.builder()
                .jobPostingId(jobPostingId)
                .stages(stages)
                .build();
    }
}
