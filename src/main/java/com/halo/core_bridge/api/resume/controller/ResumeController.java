package com.halo.core_bridge.api.resume.controller;

import com.halo.core_bridge.api.coverLetterDescription.model.dto.CoverLetterDescriptionDto;
import com.halo.core_bridge.api.coverLetterDescription.service.CoverLetterDescriptionService;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import com.halo.core_bridge.api.resume.contents.SwaggerContents;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/applies")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final CoverLetterDescriptionService coverLetterDescriptionService;

    @Operation(
            summary = "이력서 생성",
            description = "새로운 이력서를 생성하는 기능",
            requestBody = @RequestBody(
                    description = "이력서 생성 요청 데이터",
                    content = @Content(
                            schema = @Schema(implementation = ResumeDto.Create.class),
                            examples = @ExampleObject(
                                    value = SwaggerContents.RESUME_CREATE_REQUEST
                            ))))
    @ApiResponse(responseCode = "200", description = "이력서 생성 성공",
            content = @Content(
                    schema = @Schema(implementation = Long.class),
                    examples = @ExampleObject(
                            name = "이력서 생성 성공 응답",
                            value = "이력서 생성 성공"
                    )))
    @ApiResponse(responseCode = "400", description = "이력서 생성 실패", content = @Content(mediaType = "application/json"))
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
            description = "특정 이력서 정보를 조회하는 기능")
    @ApiResponse(responseCode = "200", description = "이력서 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResumeDto.Response.class),
                    examples = @ExampleObject(
                            name = "이력서 조회 성공 응답",
                            value = SwaggerContents.RESUME_RESPONSE
                    )))
    @ApiResponse(responseCode = "404", description = "이력서를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeDto.Response> getResume(@PathVariable Long resumeId) {
        ResumeDto.Response resume = resumeService.read(resumeId);
        return ResponseEntity.ok(resume);
    }

    @Operation(
            summary = "이력서 수정",
            description = "특정 이력서 정보를 수정하는 기능",
            requestBody = @RequestBody(
                    description = "이력서 수정 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResumeDto.Update.class),
                            examples = @ExampleObject(
                                    name = "이력서 수정 요청 예시",
                                    value = SwaggerContents.RESUME_UPDATE_REQUEST
                            ))))
    @ApiResponse(responseCode = "200", description = "이력서 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "이력서 수정 성공 응답",
                            value = "{\"message\": \"이력서 수정 성공\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "이력서 수정 실패", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "이력서를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{resumeId}")
    public ResponseEntity<Void> updateResume(
            @PathVariable Long resumeId,
            @RequestBody ResumeDto.Update dto) {
        resumeService.update(resumeId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "이력서 목록 조회",
            description = "모든 이력서 목록을 조회하는 기능")
    @ApiResponse(responseCode = "200", description = "이력서 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResumeDto.Response.class),
                    examples = @ExampleObject(
                            name = "이력서 목록 조회 성공 응답",
                            value = "[" + SwaggerContents.RESUME_RESPONSE + "]"
                    )))
    @GetMapping
    public ResponseEntity<List<ResumeDto.Response>> listResumes() {
        List<ResumeDto.Response> resumes = resumeService.list();
        return ResponseEntity.ok(resumes);
    }

    @Operation(
            summary = "이력서 삭제",
            description = "특정 이력서를 삭제하는 기능")
    @ApiResponse(responseCode = "200", description = "이력서 삭제 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "이력서 삭제 성공 응답",
                            value = "{\"message\": \"이력서 삭제 성공\"}"
                    )))
    @ApiResponse(responseCode = "404", description = "이력서를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {
        resumeService.delete(resumeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{resumeId}/cover-letter-descriptions")
    public ResponseEntity<List<Long>> createDescriptions(
            @PathVariable Long resumeId,
            @org.springframework.web.bind.annotation.RequestBody List<CoverLetterDescriptionDto.CoverLetterDescriptionRequest> dtoList) {

        List<Long> descriptionIds = dtoList.stream()
                .map(dto -> coverLetterDescriptionService.create(dto))
                .collect(Collectors.toList());

        return ResponseEntity.ok(descriptionIds);
    }

    @GetMapping("/{resumeId}/cover-letter-descriptions")
    public ResponseEntity<List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse>> listDescriptions(
            @PathVariable Long resumeId,
            @RequestParam Long jobPostingId) {
        List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse> descriptions =
                coverLetterDescriptionService.list(resumeId, jobPostingId);
        return ResponseEntity.ok(descriptions);
    }
}

