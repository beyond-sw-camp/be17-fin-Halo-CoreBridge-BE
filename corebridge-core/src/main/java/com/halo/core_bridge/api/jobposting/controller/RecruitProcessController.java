package com.halo.core_bridge.api.jobposting.controller;

import com.halo.core_bridge.api.jobposting.contents.SwaggerRecruitProcessContents;
import com.halo.core_bridge.api.jobposting.service.RecruitProcessService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.halo.core_bridge.api.jobposting.model.dto.RecruitProcessDto.*;

@Tag(name = "채용 공고 프로세스", description = "특정 채용 공고의 채용 단계 관련 API")
@RestController
@RequestMapping("/api/recruiter/processes")
@RequiredArgsConstructor
public class RecruitProcessController {

    private final RecruitProcessService recruitProcessService;

    @Operation(
            summary = "특정 채용 공고의 채용 단계 추가(단건 추가)",
            description = """
                    특정 채용 공고에 하나의 채용 프로세스를 추가합니다. <br>
                    채용 프로세스는 항상 마지막은 최종 발표가 오기 때문에 뒤에서 두번째 위치에 추가 됩니다.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "채용공고 등록 요청 예시",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Add.class),
                            examples = @ExampleObject(
                                    value = SwaggerRecruitProcessContents.RECRUITER_ADD_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "채용 프로세스 단건 추가 성공 응답 예시입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = recruitProcesses.class),
                                    examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_ADD_SUCCESS_RESPONSE)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<recruitProcesses>> createRecruitProcess(@RequestBody Add addRecruitProcess) {

        recruitProcessService.add(addRecruitProcess);

        recruitProcesses recruitProcesses =
                recruitProcessService.findAllRecruitProcessesByJobPostingId(addRecruitProcess.getJobPostingId());

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(recruitProcesses));
    }

    @Operation(
            summary = "특정 채용 공고의 채용 단계 목록 조회",
            description = """
                    채용 공고 ID를 이용해 해당 공고에 등록된 모든 채용 프로세스를 조회합니다. <br>
                    채용 단계의 순서는 <code>OrderIdx</code>로 정렬되어있습니다.
                    """,
            parameters = {
                    @Parameter(
                            name = "recruit",
                            description = "채용 공고 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "채용 공고 Id를 사용한 채용 프로세스 조회 성공 응답 예시입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = recruitProcesses.class),
                                    examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_GET_RESPONSE)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<BaseResponse<recruitProcesses>> getRecruitProcesses(@RequestParam("recruit") Long jobPostingId) {

        recruitProcesses findRecruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(jobPostingId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(findRecruitProcesses));
    }

    @Operation(
            summary = "채용 프로세스 순서 변경",
            description = "채용 공고 내 등록된 프로세스들의 순서를 변경합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = """
                            변경할 프로세스 순서 정보 <br>
                            <code>fromIdx</code>는 기존의 프로세스 정렬 순서 <br>
                            <code>toIdx</code>는 프로세스가 위치하고자하는 정렬 순서 <br>
                            아래의 예시를 보면 id값이 5인 채용 프로세스가 2번째 순서에서 1번째 순서로 이동합니다.
                            """,
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangeOrder.class),
                            examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_CHANGE_ORDER_REQUEST
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "채용 프로세스 순서 변경 성공 응답 예시입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = recruitProcesses.class),
                                    examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_CHANGE_ORDER_RESPONSE_SUCCESS)
                            )
                    )
            }
    )
    @PatchMapping
    public ResponseEntity<BaseResponse<Object>> changeProcessOrder(@RequestBody ChangeOrder changeOrder) {

        recruitProcessService.changeOrder(changeOrder);
        recruitProcesses recruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(changeOrder.getJobPostingId());

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(recruitProcesses));
    }

    @Operation(
            summary = "채용 프로세스 수정",
            description = "채용 프로세스의 이름과 색상을 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = """
                            수정할 프로세스 정보
                            """,
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangeOrder.class),
                            examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_UPDATE_REQUEST
                            )
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "processId",
                            description = "수정할 채용 프로세스의 id",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "채용 프로세스 수정 성공 응답 예시입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = recruitProcesses.class),
                                    examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_UPDATE_RESPONSE_SUCCESS)
                            )
                    )
            }
    )
    @PatchMapping("/{processId}")
    public ResponseEntity<BaseResponse<Object>> updateProcessName(@PathVariable Long processId, @RequestBody Update updateRecruitProcess) {

        recruitProcessService.editRecruitProcess(processId, updateRecruitProcess);
        recruitProcesses recruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(updateRecruitProcess.getJobPostingId());

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(recruitProcesses));
    }

    @Operation(
            summary = "채용 프로세스 삭제",
            description = """
                    채용 프로세스 ID를 사용하여 해당 채용 프로세스를 삭제합니다. <br>
                    지원자가 존재하면 해당 채용 프로세스를 삭제할 수 없습니다. <br>
                    <br>
                    직접 테스트를 위해서는 다음과 같은 방법이 있습니다. <br>
                    <br>
                    1. 특정 채용 공고에 프로세스를 단건 추가하는 API를 요청합니다.
                    2. 1번을 수행하면 바로 추가된 프로세스 리스트가 나오기 때문에 추가된 프로세스의 id를 사용하여 삭제를 진행하시면 됩니다.
                    <br>
                    <br>
                    """,
            parameters = {
                    @Parameter(
                            name = "processId",
                            description = "채용 프로세스 <code>id</code>",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = """
                                    채용 프로세스 삭제 성공 응답 예시입니다. <br>
                                    삭제 후 현재 채용 프로세스가 Response에 담깁니다.
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = recruitProcesses.class),
                                    examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_DELETE_RESPONSE_SUCCESS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = """
                                    채용 프로세스 삭제 실패 응답 예시입니다. <br>
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = recruitProcesses.class),
                                    examples = @ExampleObject(value = SwaggerRecruitProcessContents.RECRUITER_DELETE_RESPONSE_FAILED)
                            )
                    ),
            }
    )
    @DeleteMapping("/{processId}")
    public ResponseEntity<BaseResponse<Object>> deleteProcess(@PathVariable Long processId) {

        Long jobPostingId = recruitProcessService.deleteRecruitProcess(processId);
        recruitProcesses recruitProcesses = recruitProcessService.findAllRecruitProcessesByJobPostingId(jobPostingId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(recruitProcesses));
    }
}
