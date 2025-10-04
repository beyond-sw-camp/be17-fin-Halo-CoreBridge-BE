package com.halo.core_bridge.api.auth.service;

import com.halo.core_bridge.api.auth.model.AuthCodeMail;
import com.halo.core_bridge.api.users.service.UserService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("이메일 인증 성공")
    void verifyAuthCode() {
        // given
        AuthCodeMail authCodeMail = AuthCodeMail.builder()
                .code("123456")
                .email("test@test.com")
                .build();

        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(any(String.class))).willReturn("123456");

        willDoNothing().given(userService).existByEmail(any(String.class));

        // when
        // then
        assertDoesNotThrow(() -> authService.verifyAuthCode(authCodeMail));
    }

    @Test
    @DisplayName("잘못된 인증번호 예외 발생")
    void incorrectAuthCode() {
        // given
        AuthCodeMail authCodeMail = AuthCodeMail.builder()
                .code("123457")
                .email("test@test.com")
                .build();

        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(any(String.class))).willReturn("123456");

        willDoNothing().given(userService).existByEmail(any(String.class));

        // when
        // then
        BaseException exception = assertThrows(BaseException.class, () -> authService.verifyAuthCode(authCodeMail));
        assertThat(exception.getStatus()).isEqualTo(BaseResponseStatus.INVALID_AUTH_CODE);
    }
}