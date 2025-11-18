package com.halo.core_bridge.api.management.controller;

import com.halo.core_bridge.api.management.contents.SwaggerManagementContents;
import com.halo.core_bridge.api.management.model.dto.ManagementDto;
import com.halo.core_bridge.api.management.service.ManagementService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{jobPostingId}/management")
@Tag(name = "단계별 지원자 관리", description = "단계별 지원자 조회, 수정 API")
public class ManagementController {

    private final ManagementService managementService;

    @Operation(
            summary = "채용 공고 단계별 지원자 목록 조회",
            description = "특정 채용 공고의 지원자를 채용 단계로 그룹을 지어 조회하는 API",
            parameters = {
                    @Parameter(
                            name = "jobPostingId",
                            description = "채용 공고 Id",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "응답 성공 예시입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            value = SwaggerManagementContents.MANAGEMENT_GET_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<BaseResponse<ManagementDto>> getManagement(@PathVariable Long jobPostingId) {

        ManagementDto result = managementService.getManagement(jobPostingId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(
            summary = "지원자 채용 단계 변경",
            description = "특정 채용 공고에 지원한 지원자의 채용 단계를 변경하는 API",
            parameters = {
                    @Parameter(
                            name = "jobPostingId",
                            description = "채용 공고 Id",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "resumeId",
                            description = "이력서 Id",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "processId",
                            description = "채용 단계 Id",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "응답 성공 예시입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            value = SwaggerManagementContents.UPDATE_PROCESS_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/{resumeId}/process/{processId}")
    public ResponseEntity<BaseResponse<String>> updateProcess(
            @PathVariable Long resumeId,
            @PathVariable Long processId
    ) {
        managementService.moveApplicantProcess(resumeId, processId);
        return ResponseEntity.ok(BaseResponse.success("단계 변경 완료"));
    }
}
