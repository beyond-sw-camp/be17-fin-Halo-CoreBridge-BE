package com.halo.core_bridge.api.image.controller;

import com.halo.core_bridge.api.image.contents.SwaggerImageContents;
import com.halo.core_bridge.api.image.model.dto.ImageDto;
import com.halo.core_bridge.api.image.service.ImageService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지", description = "이미지 업로드, 조회, 삭제 API")
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 서버에 업로드합니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "업로드 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerImageContents.UPLOAD_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("directory") String directory,
            @AuthenticationPrincipal UserDto.Auth loginUser
    ) { Long memberIdx = loginUser.getId();
        ImageDto.UploadResponseDto response = imageService.uploadImage(file, directory);
        System.out.println(response);
        return ResponseEntity.ok(BaseResponse.success(response));
    }


    @Operation(
            summary = "이미지 조회",
            description = "ID로 이미지 URL을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "idx",
                            description = "이미지 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerImageContents.GET_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{idx}")
    public ResponseEntity<BaseResponse<String>> getImage(@PathVariable Long idx) {
            String result = imageService.find(idx);
            return ResponseEntity.ok(BaseResponse.success(result));
    }


    @Operation(
            summary = "이미지 삭제",
            description = "ID로 이미지를 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "idx",
                            description = "이미지 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerImageContents.DELETE_RESPONSE
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{idx}")
    public ResponseEntity<BaseResponse<Void>> deleteImage(@PathVariable Long idx) {
        imageService.deleteImage(idx);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}