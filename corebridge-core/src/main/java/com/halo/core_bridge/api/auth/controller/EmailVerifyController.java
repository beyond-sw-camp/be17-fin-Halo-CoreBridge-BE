package com.halo.core_bridge.api.auth.controller;

import com.halo.core_bridge.api.auth.contents.SwaggerAuthContents;
import com.halo.core_bridge.api.auth.model.AuthCodeMail;
import com.halo.core_bridge.api.auth.service.AuthService;
import com.halo.core_bridge.api.mail.service.AuthCodeMailService;
import com.halo.core_bridge.api.users.service.UserService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "이메일 인증 관련 API")
public class EmailVerifyController {

    private final AuthCodeMailService authCodeMailService;
    private final AuthService authService;
    private final UserService userService;

    /**
     * 이메일로 인증번호 보내기
     * @param email 전송할 이메일
     */
    @Operation(
            summary = "인증 코드 전송",
            description = "인증 코드를 이메일로 전송합니다.",
            parameters = {
                    @Parameter(
                            name = "email",
                            description = "인증할 이메일 주소, 실제 이메일을 입력해주세요.",
                            in = ParameterIn.QUERY,
                            required = true,
                            example = "test@corebridge.com"
                    ),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "인증 코드 전송 성공",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "인증 코드 전송 성공 응답",
                                            value = SwaggerAuthContents.SEND_AUTH_CODE_RESPONSE_SUCCESS
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "인증 코드 전송 실패",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "인증 코드 전송 실패 응답",
                                            value = SwaggerAuthContents.SEND_AUTH_CODE_RESPONSE_FAILED
                                    )
                            )
                    )
            }
    )
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
    @Operation(
            summary = "인증 코드 검증",
            description = "이메일로 전송된 인증 코드를 검증합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일 인증 코드 검증 데이터, 인증 코드 전송 API를 사용해 실제 이메일로 전송된 인증 코드와 해당 이메일을 입력해주세요.",
                    content = @Content(
                            schema = @Schema(implementation = AuthCodeMail.class),
                            examples = @ExampleObject(
                                    value = SwaggerAuthContents.AUTH_CODE
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "인증 성공",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "인증 성공 응답",
                                            value = SwaggerAuthContents.AUTH_CODE_AUTHENTICATE_RESPONSE_SUCCESS
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "인증 실패",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "인증 실패 응답",
                                            value = SwaggerAuthContents.AUTH_CODE_AUTHENTICATE_RESPONSE_FAILED
                                    )
                            )
                    )
            }
    )
    @PostMapping("/email/verify-code")
    public ResponseEntity<BaseResponse<Object>> verifyCode(@RequestBody AuthCodeMail authCodeMail) {

        authService.verifyAuthCode(authCodeMail);
        return ResponseEntity.ok(BaseResponse.success("이메일 인증 성공"));
    }
}
