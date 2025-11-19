package com.halo.core_bridge.api.auth.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AuthCodeCreator {

    private static final int NUMBER_BOUND = 1000000;

    /**
     * 인증 번호를 생성한다.
     * @return <code>AuthCode</code> - 문자열 타입의 인증번호를 담은 객체
     */
    public String generateAuthCode() {
        SecureRandom secureRandom = new SecureRandom();
        int randomCode = secureRandom.nextInt(NUMBER_BOUND);

        return String.format("%06d", randomCode);
    }
}
