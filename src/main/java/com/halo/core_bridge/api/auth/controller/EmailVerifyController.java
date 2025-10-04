package com.halo.core_bridge.api.auth.controller;

import com.halo.core_bridge.api.auth.model.AuthCodeMail;
import com.halo.core_bridge.api.auth.service.AuthService;
import com.halo.core_bridge.api.mail.service.AuthCodeMailService;
import com.halo.core_bridge.api.users.service.UserService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailVerifyController {

    private final AuthCodeMailService authCodeMailService;
    private final AuthService authService;
    private final UserService userService;

    /**
     * 이메일로 인증번호 보내기
     * @param email 전송할 이메일
     */
    @GetMapping("/email/verify-code")
    public ResponseEntity<BaseResponse<Object>> sendAuthCode(String email) {

        // 이메일 중복 검사
        userService.existByEmail(email);

        // 이메일 전송
        authCodeMailService.sendToEmail(email);
        return ResponseEntity.ok(BaseResponse.success("인증 번호 전송 성공"));
    }

    /**
     * 인증번호 검증
     * @param authCodeMail 검증하기 위한 이메일과 인증 번호를 담은 <code>DTO</code>
     */
    @PostMapping("/email/verify-code")
    public ResponseEntity<BaseResponse<Object>> verifyCode(@RequestBody AuthCodeMail authCodeMail) {

        authService.verifyAuthCode(authCodeMail);
        return ResponseEntity.ok(BaseResponse.success("이메일 인증 성공"));
    }
}
