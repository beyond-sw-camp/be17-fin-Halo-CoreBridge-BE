package com.halo.core_bridge.api.jobposting.controller;


import com.halo.core_bridge.api.coverLetterTitle.service.CoverLetterTitleService;
import com.halo.core_bridge.api.interview.service.InterviewerService;
import com.halo.core_bridge.api.jobposting.contents.SwaggerJobPostingContents;
import com.halo.core_bridge.api.jobposting.service.JobPostingEsService;
import com.halo.core_bridge.api.jobposting.service.JobPostingService;
import com.halo.core_bridge.api.jobposting.service.JobPostingSkillService;
import com.halo.core_bridge.api.jobposting.service.RecruitProcessService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-postings")
@Tag(name = "채용 공고", description = "채용공고 등록, 조회, 상세조회 관련 API")
public class JobPostingController {
    private final JobPostingService jobPostingService;
    private final JobPostingSkillService jobPostingSkillService;
    private final RecruitProcessService recruitProcessService;
    private final CoverLetterTitleService coverLetterTitleService;
    private final InterviewerService interviewerService;
    private final JobPostingEsService jobPostingEsService;

    //채용공고 등록
    @Operation(
            summary = "채용공고 등록",
            description = "새로운 채용공고를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "채용공고 등록 요청 예시",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateRequest.class),
                            examples = @ExampleObject(
                                    name = "JobPosting Create Example",
                                    value = SwaggerJobPostingContents.JOB_POSTING_CREATE_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "등록 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_CREATE_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 실패 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_VALIDATION_ERROR_RESPONSE
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<String>> createJobPosting(
//            @AuthenticationPrincipal UserDto.Auth auth,
            @RequestBody @Validated CreateRequest request) {
        Long userId = 1L;
        //채용공고 저장후 채용공고id 값 저장
        Long jobPostingId = jobPostingService.save(request, userId);

        // 다대일로 연결된 테이블에 각각 jobPostingId와 함께 서비스 호출
        jobPostingSkillService.saveAll(request.getTechStack(), jobPostingId);
        recruitProcessService.create(request.getRecruitProcess(), jobPostingId);
        coverLetterTitleService.create(request.getCoverLetterTitles(), jobPostingId);

        // 면접관 등록
        interviewerService.saveAll(jobPostingId, request.getInterviewers());

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("채용공고 등록완료"));
    }

    //채용공고 전체 목록 조회
    @Operation(
            summary = "채용공고 전체 목록 조회",
            description = "등록된 모든 채용공고를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_LIST_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "등록된 채용공고가 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 실패 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTINGS_NOT_FOUND_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity
            <BaseResponse<List<JobPostingListResponseDto>>> getAllJobPostings() {
        List<JobPostingListResponseDto> list = jobPostingService.getJobPostingList();
        return ResponseEntity.ok(BaseResponse.success(list));
    }

    //채용공고 상세 조회
    @Operation(
            summary = "채용공고 상세 조회",
            description = "특정 ID의 채용공고를 상세 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "채용공고 ID",
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
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_DETAIL_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 채용공고",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_NOT_FOUND_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DetailResponse>> getJobPostingDetail(@PathVariable Long id) {
        DetailResponse detail = jobPostingService.getDetail(id);
        return ResponseEntity.ok(BaseResponse.success(detail)); // HTTP 200 OK
    }

    //채용공고 기본정보 조회
    @Operation(
            summary = "채용공고 기본정보 조회",
            description = "특정 채용 상세 화면에서 채용의 기본 정보가 담긴 Header부분에 필요한 기본정보를 채용 ID로 조회합니다.",
            parameters = {
              @Parameter(
                      name = "id",
                      description = "채용공고 ID",
                      required = true,
                      example = "1"
              )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 성공 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_BASIC_RESPONSE
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 채용공고",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "요청 실패 응답 예시입니다.",
                                            value = SwaggerJobPostingContents.JOB_POSTING_NOT_FOUND_RESPONSE
                                    )
                            )
                    )
            }
    )
    @GetMapping("/header/{id}")
    public ResponseEntity<BaseResponse<HeaderResponse>>  getJobPostingHeader(@PathVariable Long id) {
        HeaderResponse headerDetail = jobPostingService.getHeaderDetail(id);
        return ResponseEntity.ok(BaseResponse.success(headerDetail));
    }


    //채용공고 update를 위한 바꾸고자 하는 채용공고 데이터 가공 없이 그대로 반환용 end-point
    @GetMapping("/{id}/edit")
    public ResponseEntity<BaseResponse<EditResponse>> getJobPostingForEdit(@PathVariable Long id) {
        EditResponse result = jobPostingService.getEditResponse(id);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    //채용공고 수정
    @Operation(
            summary = "채용공고 수정",
            description = "기존 채용공고를 수정합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "채용공고 ID",
                            required = true,
                            example = "1"
                    )
            },
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
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> updateJobPosting(@PathVariable Long id,
                                                                 @Valid @RequestBody UpdateRequest request) {
        jobPostingService.updateJobPosting(id, request);
        return ResponseEntity.ok(BaseResponse.success("수정 완료"));
    }

    //채용공고 삭제
    @DeleteMapping("/{id}")
    @Operation(
            summary = "채용공고 삭제",
            description = "기존 채용공고를 삭제합니다.",
            parameters = {
              @Parameter(
                      name = "id",
                      description = "채용공고 ID",
                      required = true,
                      example = "1"
              )
            },
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

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<JobPostingPage>> searchJobPostings(@RequestParam(required = false) String keyword,
                                                                          @RequestParam(required = false, name = "search_type", defaultValue = "sql") String searchType,
                                                                          @RequestParam(required = false, defaultValue = "0") int page) {

        SearchQuery searchQuery = SearchQuery.from(keyword, page);

        JobPostingPage jobPostingPages = null;

        if (searchType.equals("es")) {
            jobPostingPages = jobPostingEsService.searchJobPostings(searchQuery);
        } else if(searchType.equals("sql")) {
            jobPostingPages = jobPostingService.searchJobPostings(searchQuery);
        }

        return ResponseEntity.ok(BaseResponse.success(jobPostingPages));
    }
}
