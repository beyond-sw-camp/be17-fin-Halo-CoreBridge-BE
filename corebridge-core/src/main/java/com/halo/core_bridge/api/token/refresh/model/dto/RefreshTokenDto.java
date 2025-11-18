package com.halo.core_bridge.api.token.refresh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RefreshTokenDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private Long userId;
        private String role;

        public static Meta from(Long userId, String role) {
            return Meta.builder()
                    .userId(userId)
                    .role(role)
                    .build();
        }
    }
}
