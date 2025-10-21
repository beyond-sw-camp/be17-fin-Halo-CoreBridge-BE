package com.halo.core_bridge.api.auth.model;

import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Getter
    public static class SendEmail {
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPassword {
        private String email;
        private String password;
        private String token;
    }

    @Getter
    public static class FindEmailReq {

        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @NotBlank(message = "연락처를 입력하세요.")
        @Pattern(
                regexp = "^010-\\d{4}-\\d{4}$",
                message = "올바른 전화번호 형식으로 입력해주세요."
        )
        private String phone;
    }

    @Getter
    @Builder
    public static class FindEmailResp {

        private String findEmail;

        public static FindEmailResp from(User user) {
            return FindEmailResp.builder()
                    .findEmail(user.getEmail())
                    .build();
        }
    }
}
