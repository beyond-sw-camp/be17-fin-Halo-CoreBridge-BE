package com.halo.core_bridge.api.auth.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthCodeMail {
    private String code;
    private String email;
}
