package com.halo.core_bridge.api.auth.service;

import com.halo.core_bridge.api.auth.model.AuthCodeMail;
import com.halo.core_bridge.api.mail.model.MailSend;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 인증 번호를 검증한다.
     * @param authCodeMail 이메일과 인증 코드가 담긴 dto
     * @throws BaseException 유효하지 않은 인증번호일 때 예외 발생
     */
    public void verifyAuthCode(AuthCodeMail authCodeMail) {

        String redisKey = MailSend.AUTH_CODE_MAIL.createRedisKey(authCodeMail.getEmail());
        String findAuthCode = getValue(redisKey);

        if (findAuthCode == null || !findAuthCode.equals(authCodeMail.getCode())) {
            throw BaseException.from(BaseResponseStatus.INVALID_AUTH_CODE);
        }

        deleteByRedisKey(redisKey);
    }

    private String getValue(String redisKey) {
        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    private void deleteByRedisKey(String redisKey) {
        stringRedisTemplate.delete(redisKey);
    }
}
