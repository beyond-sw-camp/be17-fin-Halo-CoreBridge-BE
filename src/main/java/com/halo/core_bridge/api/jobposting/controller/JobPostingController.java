package com.halo.core_bridge.api.jobposting.controller;


import com.halo.core_bridge.api.jobposting.contents.SwaggerJobPostingContents;
import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.service.JobPostingService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-postings")
@Tag(name = "채용공고 API", description = "채용공고 등록, 조회, 상세조회 관련 API")
public class JobPostingController {
    private final JobPostingService jobPostingService;

    //채용공고 등록
    @Operation(
            summary = "채용공고 등록",
            description = "새로운 채용공고를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "채용공고 등록 요청 예시",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = JobPostingDto.CreateRequest.class),
                            examples = @ExampleObject(
                                    name = "JobPosting Create Example",
                                    value = SwaggerJobPostingContents.JOB_POSTING_CREATE_REQUEST
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Success Response",
                                    value = SwaggerJobPostingContents.JOB_POSTING_CREATE_RESPONSE
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Validation Error Response",
                                    value = SwaggerJobPostingContents.JOB_POSTING_VALIDATION_ERROR_RESPONSE
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<BaseResponse<String>> createJobPosting(
//            @AuthenticationPrincipal UserDto.Auth auth,
            @RequestBody @Validated JobPostingDto.CreateRequest request) {
        Long userId = 1L;
        jobPostingService.save(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("채용공고 등록완료"));
    }

    //채용공고 전체 목록 조회
    @Operation(
            summary = "채용공고 전체 목록 조회",
            description = "등록된 모든 채용공고를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "List Response Example",
                                    value = SwaggerJobPostingContents.JOB_POSTING_LIST_RESPONSE
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "등록된 채용공고가 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "JobPostings Not Found Response",
                                    value = SwaggerJobPostingContents.JOB_POSTINGS_NOT_FOUND_RESPONSE
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity
            <BaseResponse<List<JobPostingDto.JobPostingListResponseDto>>> getAllJobPostings() {
        List<JobPostingDto.JobPostingListResponseDto> list = jobPostingService.getJobPostingList();
        return ResponseEntity.ok(BaseResponse.success(list));
    }

    //채용공고 상세 조회
    @Operation(
            summary = "채용공고 상세 조회",
            description = "특정 ID의 채용공고를 상세 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Detail Response Example",
                                    value = SwaggerJobPostingContents.JOB_POSTING_DETAIL_RESPONSE
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채용공고",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Not Found Response",
                                    value = SwaggerJobPostingContents.JOB_POSTING_NOT_FOUND_RESPONSE
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostingDto.DetailResponse>> getJobPostingDetail(@PathVariable Long id) {
        JobPostingDto.DetailResponse detail = jobPostingService.getDetail(id);
        return ResponseEntity.ok(BaseResponse.success(detail)); // HTTP 200 OK
    }

    @Operation(
            summary = "채용공고 기본정보 조회",
            description = "특정 ID의 채용공고의 기본정보를 조회합니다. Header부분"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Detail Response Example",
                                    value = SwaggerJobPostingContents.JOB_POSTING_BASIC_RESPONSE
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채용공고",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Not Found Response",
                                    value = SwaggerJobPostingContents.JOB_POSTING_NOT_FOUND_RESPONSE
                            )
                    )
            )
    })
    @GetMapping("/header/{id}")
    public ResponseEntity<BaseResponse<JobPostingDto.HeaderResponse>>  getJobPostingHeader(@PathVariable Long id) {
        JobPostingDto.HeaderResponse headerDetail = jobPostingService.getHeaderDetail(id);
        return ResponseEntity.ok(BaseResponse.success(headerDetail));
    }


    //채용공고 수정
    @PatchMapping("/{id}")
    @Operation(
            summary = "채용공고 수정",
            description = "기존 채용공고를 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "채용공고 수정 요청 예시",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = SwaggerJobPostingContents.JOB_POSTING_UPDATE_REQUEST)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "채용공고 수정 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = SwaggerJobPostingContents.JOB_POSTING_UPDATE_RESPONSE)
                            )
                    )
            }
    )
    public ResponseEntity<BaseResponse<String>> updateJobPosting(@PathVariable Long id, @Valid @RequestBody JobPostingDto.UpdateRequest request) {
        jobPostingService.updateJobPosting(id, request);
        return ResponseEntity.ok(BaseResponse.success("수정 완료"));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "채용공고 삭제",
            description = "기존 채용공고를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "채용공고 삭제 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = SwaggerJobPostingContents.JOB_POSTING_DELETE_RESPONSE)
                            )
                    )
            }
    )
    public ResponseEntity<BaseResponse<String>> deleteJobPosting(
            @PathVariable Long id
    ) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.ok(BaseResponse.success("채용공고 삭제 완료"));
    }

}
