package com.halo.core_bridge.api.resume.controller;

import com.halo.core_bridge.api.coverLetterDescription.model.dto.CoverLetterDescriptionDto;
import com.halo.core_bridge.api.coverLetterDescription.service.CoverLetterDescriptionService;
import com.halo.core_bridge.api.resume.contents.SwaggerResumeContents;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.service.ResumeService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "이력서", description = "이력서 및 자기소개서 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/jobposts/{jobpostId}/applies")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final CoverLetterDescriptionService coverLetterDescriptionService;

    @Operation(summary = "이력서 생성", description = "새로운 이력서를 생성합니다.",
            parameters = {
                    @Parameter(
                            name = "jobpostId",
                            description = "채용 공고 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    description = "이력서 생성 요청 데이터",
                    content = @Content(
                            schema = @Schema(implementation = ResumeDto.Create.class),
                            examples = @ExampleObject(
                                    value = SwaggerResumeContents.RESUME_CREATE_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "이력서 생성 성공",
                            content = @Content(
                                    schema = @Schema(implementation = Long.class),
                                    examples = @ExampleObject(
                                            name = "이력서 생성 성공 응답",
                                            value = SwaggerResumeContents.RESUME_CREATE_REQUEST
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Long> createResume(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("resume") ResumeDto.Create dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        Long userId = Long.parseLong(userDetails.getUsername());
        Long resumeId = resumeService.create(dto, userId, file);
        return ResponseEntity.ok(resumeId);
    }

    @Operation(
            summary = "이력서 조회",
            description = "특정 이력서 정보를 조회하는 기능",
            parameters = {
                    @Parameter(
                            name = "resumeId",
                            description = "이력서 Id",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "이력서 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResumeDto.Response.class),
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerResumeContents.RESUME_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeDto.Response> getResume(@PathVariable Long resumeId) {
        ResumeDto.Response resume = resumeService.read(resumeId);
        return ResponseEntity.ok(resume);
    }

    @Operation(summary = "이력서 수정", description = "ID로 특정 이력서를 수정합니다.",
            parameters = {
                    @Parameter(name = "jobpostId", description = "채용 공고 ID", required = true, example = "1"),
                    @Parameter(name = "resumeId", description = "이력서 ID", required = true, example = "1")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResumeDto.Update.class),
                            examples = @ExampleObject(value = SwaggerResumeContents.RESUME_UPDATE_REQUEST))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerResumeContents.RESUME_UPDATE_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/{resumeId}")
    public ResponseEntity<Void> updateResume(
            @PathVariable Long resumeId,
            @RequestBody ResumeDto.Update dto) {
        resumeService.update(resumeId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "이력서 목록 조회",
            description = "특정 채용 공고의 채용 공고 ID로 이력서 목록을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "jobpostId",
                            description = "채용 공고 ID",
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
                                            value = SwaggerResumeContents.RESUME_LIST_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ResumeDto.Response>> listResumes() {
        List<ResumeDto.Response> resumes = resumeService.list();
        return ResponseEntity.ok(resumes);
    }

    @Operation(
            summary = "이력서 삭제",
            description = "ID로 특정 이력서를 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "jobpostId",
                            description = "채용 공고 ID",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "resumeId",
                            description = "이력서 ID",
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
                                            value = SwaggerResumeContents.RESUME_DELETE_RESPONSE)
                            )
                    )
            }
    )
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {
        resumeService.delete(resumeId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "자기소개서 항목 답변 생성",
            description = "이력서에 자기소개서 항목에 대한 답변을 추가합니다.",
            parameters = {
                    @Parameter(
                            name = "jobpostId",
                            description = "채용 공고 ID",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "resumeId",
                            description = "이력서 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = @ExampleObject(
                                    value = SwaggerResumeContents.COVER_LETTER_CREATE_REQUEST)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerResumeContents.COVER_LETTER_CREATE_RESPONSE)
                            )
                    )
            }
    )
    @PostMapping("/{resumeId}/cover-letter-descriptions")
    public ResponseEntity<List<Long>> createDescriptions(
            @PathVariable Long resumeId,
            @org.springframework.web.bind.annotation.RequestBody List<CoverLetterDescriptionDto.CoverLetterDescriptionRequest> dtoList) {

        List<Long> descriptionIds = dtoList.stream()
                .map(dto -> coverLetterDescriptionService.create(dto))
                .collect(Collectors.toList());

        return ResponseEntity.ok(descriptionIds);
    }

    @Operation(
            summary = "자기소개서 항목 답변 목록 조회",
            description = "이력서의 자기소개서 항목 답변 목록을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "jobpostId",
                            description = "채용 공고 ID",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "resumeId",
                            description = "이력서 ID",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "jobPostingId",
                            description = "채용 공고 ID",
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
                                            value = SwaggerResumeContents.COVER_LETTER_LIST_RESPONSE)
                            )
                    )
            }
    )
    @GetMapping("/{resumeId}/cover-letter-descriptions")
    public ResponseEntity<List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse>> listDescriptions(
            @PathVariable Long resumeId,
            @RequestParam Long jobPostingId) {
        List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse> descriptions =
                coverLetterDescriptionService.list(resumeId, jobPostingId);
        return ResponseEntity.ok(descriptions);
    }
}
