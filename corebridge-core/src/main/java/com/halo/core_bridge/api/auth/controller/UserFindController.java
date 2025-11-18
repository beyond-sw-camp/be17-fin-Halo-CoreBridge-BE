package com.halo.core_bridge.api.auth.controller;

import com.halo.core_bridge.api.auth.contents.SwaggerAuthContents;
import com.halo.core_bridge.api.auth.model.AuthDto;
import com.halo.core_bridge.api.auth.service.UserFindService;
import com.halo.core_bridge.api.mail.service.PasswordResetMailService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name ="회원 정보 찾기", description = "사용자의 이메일과 비밀번호를 찾는 API")
public class UserFindController {

    private final PasswordResetMailService passwordResetMailService;
    private final UserFindService userFindService;

    @Operation(
            summary = "비밀번호 찾기 링크 전송",
            description = "사용자가 가입한 이메일로 비밀번호 찾기 링크를 전송합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "비밀번호 재설정 링크를 받을 이메일 주소",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthDto.SendEmail.class),
                            examples = @ExampleObject(
                                    value = SwaggerAuthContents.SEND_EMAIL_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "링크 전송 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            value = SwaggerAuthContents.SEND_EMAIL_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping("/find-password/link")
    public ResponseEntity<BaseResponse<Object>> sendAuthCodeForResetPassword(@RequestBody AuthDto.SendEmail emailForPwdRest) {
        String email = emailForPwdRest.getEmail();
        passwordResetMailService.sendToEmail(email);

        return ResponseEntity.ok(BaseResponse.success("비밀번호 재설정 링크 전송 성공"));
    }

    @Operation(
            summary = "비밀번호 재설정",
            description = """
                    이메일로 전송된 비밀번호 변경 링크의 RequestParameter인 token의 값과 이메일을 사용하여 정상적인 비밀번호 요청인지 검증 후 비밀번호를 변경합니다. <br>
                    uuid는 이메일로 번송된 <code>비밀번호 변경 url?token={uuid}</code>의 값입니다.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "비밀번호 재설정 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthDto.ResetPassword.class),
                            examples = @ExampleObject(
                                    value = SwaggerAuthContents.RESET_PASSWORD_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "재설정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            value = SwaggerAuthContents.RESET_PASSWORD_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse<Object>> resetPassword(@RequestBody AuthDto.ResetPassword resetPassword) {

        userFindService.resetPassword(resetPassword);

        return ResponseEntity.ok(BaseResponse.success("재설정 성공"));
    }

    @Operation(
            summary = "이메일 찾기",
            description = "이름과 연락처로 가입된 이메일을 찾습니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일 찾기 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthDto.FindEmailReq.class),
                            examples = @ExampleObject(
                                    value = SwaggerAuthContents.FIND_EMAIL_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 찾기 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            value = SwaggerAuthContents.FIND_EMAIL_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping("/find-email")
    public ResponseEntity<BaseResponse<Object>> findEmail(@RequestBody AuthDto.FindEmailReq findEmailInfo) {

        AuthDto.FindEmailResp findUser = userFindService.findEmailByNameAndPhoneNumber(findEmailInfo);

        return ResponseEntity.ok(BaseResponse.success(findUser));
    }
}
