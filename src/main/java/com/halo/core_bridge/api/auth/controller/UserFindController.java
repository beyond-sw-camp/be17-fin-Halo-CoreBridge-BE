package com.halo.core_bridge.api.auth.controller;

import com.halo.core_bridge.api.auth.model.AuthDto;
import com.halo.core_bridge.api.auth.service.UserFindService;
import com.halo.core_bridge.api.mail.service.PasswordResetMailService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name ="아이디와 비밀번호 찾기 기능")
public class UserFindController {

    private final PasswordResetMailService passwordResetMailService;
    private final UserFindService userFindService;

    @PostMapping("/find-password/link")
    public ResponseEntity<BaseResponse<Object>> sendAuthCodeForResetPassword(@RequestBody AuthDto.SendEmail emailForPwdRest) {
        String email = emailForPwdRest.getEmail();
        passwordResetMailService.sendToEmail(email);

        return ResponseEntity.ok(BaseResponse.success("비밀번호 재설정 링크 전송 성공"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse<Object>> resetPassword(@RequestBody AuthDto.ResetPassword resetPassword) {

        userFindService.resetPassword(resetPassword);

        return ResponseEntity.ok(BaseResponse.success("재설정 성공"));
    }

    @PostMapping("/find-email")
    public ResponseEntity<BaseResponse<Object>> findEmail(@RequestBody AuthDto.FindEmailReq findEmailInfo) {

        AuthDto.FindEmailResp findUser = userFindService.findEmailByNameAndPhoneNumber(findEmailInfo);

        return ResponseEntity.ok(BaseResponse.success(findUser));
    }
}
