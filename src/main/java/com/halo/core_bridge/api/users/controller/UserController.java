package com.halo.core_bridge.api.users.controller;

import com.halo.core_bridge.api.users.contents.SwaggerUserContents;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.api.users.service.UserService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "회원 기능")
public class UserController {

    private final UserService userService;

    @Value("${app.token.access.name}")
    private String accessToken;

    @Value("${app.token.refresh.name}")
    private String refreshToken;

    @Operation(
            summary = "회원 가입",
            description = "회원 가입을 진행합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원 가입 요청 데이터",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.Create.class),
                            examples = @ExampleObject(
                                    value = SwaggerUserContents.USER_CREATE
                            ))))
    @ApiResponse(responseCode = "200", description = "회원 가입 성공",
            content = @Content(
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(
                            name = "회원 가입 성공 응답",
                            value = SwaggerUserContents.RESPONSE_SUCCESS
                    )))
    @ApiResponse(responseCode = "400", description = "회원 가입 실패",
            content = @Content(
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(
                            name = "회원 가입 실패 응답",
                            value = SwaggerUserContents.RESPONSE_FAILED
                    )))
    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createUser(@Valid @RequestBody UserDto.Create create) {
        userService.save(create);

        return ResponseEntity.ok(BaseResponse.success("회원 가입 성공"));
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponse<UserDto.Read>> getUserDetail(@AuthenticationPrincipal UserDto.Auth auth) {

        UserDto.Read findReadUser = userService.findById(auth.getId());
        return ResponseEntity.ok(BaseResponse.success(findReadUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Object>> logout() {

        ResponseCookie accessTokenCookie = ResponseCookie.from(this.accessToken, null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(this.refreshToken, null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString(), refreshTokenCookie.toString())
                .body(BaseResponse.success(null));
    }

    @GetMapping("/resume-info")
    public ResponseEntity<BaseResponse<UserDto.ResumeUserInfo>> getResumeInfo(@AuthenticationPrincipal UserDto.Auth auth) {
        UserDto.ResumeUserInfo info = userService.findForResumeInfo(auth.getId());
        return ResponseEntity.ok(BaseResponse.success(info));
    }
}
