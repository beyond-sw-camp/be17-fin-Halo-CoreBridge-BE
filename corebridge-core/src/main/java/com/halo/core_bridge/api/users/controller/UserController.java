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
@Tag(name = "회원 관리", description = "회원 조회, 수정, 삭제, 등록 API")
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
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserDto.Create.class),
                            examples = @ExampleObject(value = SwaggerUserContents.USER_CREATE)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 가입 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerUserContents.RESPONSE_SUCCESS
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원 가입 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "요청 실패 응답 예시입니다.",
                                            value = SwaggerUserContents.RESPONSE_FAILED
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createUser(@Valid @RequestBody UserDto.Create create) {
        userService.save(create);

        return ResponseEntity.ok(BaseResponse.success("회원 가입 성공"));
    }

    @Operation(
            summary = "회원 정보 조회",
            description = "현재 로그인한 회원의 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerUserContents.USER_DETAIL_RESPONSE)
                            )
                    )
            }
    )
    @GetMapping("/info")
    public ResponseEntity<BaseResponse<UserDto.Read>> getUserDetail(@AuthenticationPrincipal UserDto.Auth auth) {

        UserDto.Read findReadUser = userService.findById(auth.getId());
        return ResponseEntity.ok(BaseResponse.success(findReadUser));
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 처리하고 쿠키를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그아웃 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerUserContents.LOGOUT_RESPONSE)
                            )
                    )
            }
    )
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


    @Operation(
            summary = "이력서용 회원 정보 조회",
            description = """
                    이력서 작성에 필요한 최소한의 회원 정보를 조회합니다. <br>
                    로그인한 사용자만 접근 가능합니다. <br>
                    로그인 API 호출 후 쿠키에 반환된 USER_AT를 Swagger의 Authorize에 저장한뒤 테스트 해주세요.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerUserContents.RESUME_USER_INFO_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping("/resume-info")
    public ResponseEntity<BaseResponse<UserDto.ResumeUserInfo>> getResumeInfo(@AuthenticationPrincipal UserDto.Auth auth) {
        UserDto.ResumeUserInfo info = UserDto.ResumeUserInfo.from(userService.findForResumeInfo(auth.getId()));
        return ResponseEntity.ok(BaseResponse.success(info));
    }
}
