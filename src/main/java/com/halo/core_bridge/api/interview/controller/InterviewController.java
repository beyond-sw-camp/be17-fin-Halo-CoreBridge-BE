package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.contents.SwaggerInterviewContents;
import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.service.InterviewService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruiter/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @Operation(
            summary = "면접 등록",
            description = "면접을 등록 하는 기능입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "면접 등록 요청 데이터",
                    content = @Content(
                            schema = @Schema(implementation = InterviewDto.Create.class),
                            examples = @ExampleObject(
                                    value = SwaggerInterviewContents.INTERVIEW_CREATE
                            ))))
    @ApiResponse(responseCode = "200", description = "면접 등록 성공하는 경우의 응답",
            content = @Content(
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(
                            name = "면접 성공 성공 응답",
                            value = SwaggerInterviewContents.RESPONSE_SUCCESS
                    )))
    @ApiResponse(responseCode = "400", description = "면접 등록 실패 시 응답",
            content = @Content(
                    schema = @Schema(implementation = BaseResponse.class)
//                    examples = @ExampleObject(
//                            name = "면접 등록 실패 응답",
//                            value = SwaggerUserContents.RESPONSE_FAILED
//                    )
                    ))
    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createInterview(@RequestBody InterviewDto.Create interviewRequest) {

        interviewService.save(interviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("면접을 등록하였습니다."));
    }
}
