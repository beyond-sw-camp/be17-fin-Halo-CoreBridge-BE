package com.halo.core_bridge.api.auth.model;

import lombok.Getter;

public class AuthDto {

    @Getter
    public static class SendEmail {
        private String email;
    }

    @Getter
    public static class ResetPassword {
        private String email;
        private String password;
        private String token;
    }
}
