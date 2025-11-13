package com.halo.core_bridge.api.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TeckStackDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeckStackResponse {
        private String code; //SPRING_BOOT
        private String label; // Spring Boot
    }
}
