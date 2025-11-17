package com.halo.core_bridge.api.jobposting.controller;

import com.halo.core_bridge.api.jobposting.contents.SwaggerPublicJobPostingContents;
import com.halo.core_bridge.api.jobposting.model.dto.PublicJobPostingDto;
import com.halo.core_bridge.api.jobposting.service.JobPostingPublicService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공개 채용 공고", description = "공개용 채용 공고 목록 조회 API")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class PublicJobPostingController {

    private final JobPostingPublicService jobPostingPublicService;

    @Operation(
            summary = "공개 채용 공고 목록 조회",
            description = "모든 권한을 가진 사용자가 볼 수 있는 현재 진행 중인 모든 채용 공고 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            description = "요청 성공 응답 예시입니다.",
                                            value = SwaggerPublicJobPostingContents.GET_PUBLIC_JOB_POSTING_LIST_RESPONSE
                                    )
                            )
                    )

            }
    )
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Object>> getPublicJobPostingList(
            @ModelAttribute PublicJobPostingDto.PublicJobSearchRequest req
    ) {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        PublicJobPostingDto.Jobs response =
                jobPostingPublicService.searchPublicJobs(req, pageable);

        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
