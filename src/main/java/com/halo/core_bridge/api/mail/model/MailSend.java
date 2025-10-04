package com.halo.core_bridge.api.mail.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailSend {
    AUTH_CODE_MAIL("[CoreBridge] 안녕하세요. 요청하신 인증번호를 보내드립니다.", "AUTH_CODE_MAIL:");

    private final String subject;
    private final String keyName;

    public String createRedisKey(String param) {
        return keyName.concat(param);
    }
}
