package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.contents.SwaggerInterviewContents;
import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.interview.service.InterviewService;
import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.*;

@Tag(name = "면접", description = "면접 등록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruiter/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @Operation(
            summary = "면접 등록",
            description = "면접을 등록하는 기능입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "면접 등록 요청 데이터",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Create.class),
                            examples = @ExampleObject(value = SwaggerInterviewContents.INTERVIEW_CREATE)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "면접 등록 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerInterviewContents.RESPONSE_SUCCESS)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createInterview(@RequestBody Create interviewRequest) {
        interviewService.save(interviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("면접을 등록하였습니다."));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Interviews>> getInterviews(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(required = false, name = "search") String keyword,
                                                                  @RequestParam(required = false) InterviewStatus status,
                                                                  @AuthenticationPrincipal UserDto.Auth auth) {

        if (auth.getRole().equals(UserRoleType.ROLE_INTERVIEWER.name())) {

            Interviews search = interviewService.searchV2(
                    SearchQuery.from(page, status, keyword, auth.getId())
            );

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(search));
        }

        Interviews search = interviewService.search(SearchQuery.from(page, status, keyword));
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(search));
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<BaseResponse<Read>> getInterview(@PathVariable("interviewId") Long interviewId) {

        Read findInterview = interviewService.findByInterviewId(interviewId);
        return ResponseEntity.ok(BaseResponse.success(findInterview));
    }

    @PostMapping("/{interviewId}/cancel")
    public ResponseEntity<BaseResponse<Object>> cancelInterview(@PathVariable("interviewId") Long interviewId,
                                                                @RequestBody InterviewDto.Cancel cancelRequest) {

        interviewService.cancelInterview(interviewId, cancelRequest);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("면접을 취소하였습니다."));
    }
}
