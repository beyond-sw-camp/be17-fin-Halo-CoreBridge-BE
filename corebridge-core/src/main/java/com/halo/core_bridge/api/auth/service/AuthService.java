package com.halo.core_bridge.api.auth.service;

import com.halo.core_bridge.api.auth.model.AuthCodeMail;
import com.halo.core_bridge.api.auth.model.AuthDto;
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

    /**
     * 비밀번호를 재설정 하기 전 올바른 인증 토큰인지 확인.
     * @param resetPassword 비밀번호 재설정을 위한 DTO
     * @throws BaseException 유효하지 않은 토큰인 경우 예외 발생
     */
    public void verifyUserPasswordReset(AuthDto.ResetPassword resetPassword) {

        String redisKey = MailSend.PASSWORD_RESET_MAIL.createRedisKey(resetPassword.getEmail());
        String findUuidByEmail = getValue(redisKey);

        // redisKey로 조회한 값이 null이라면 예외 처리
        if (findUuidByEmail == null) {
            throw BaseException.from(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        // uuid가 일치하지 않는 경우 예외처리
        if (!resetPassword.getToken().equals(findUuidByEmail)) {
            throw BaseException.from(BaseResponseStatus.INVALID_REFRESH_TOKEN);
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
