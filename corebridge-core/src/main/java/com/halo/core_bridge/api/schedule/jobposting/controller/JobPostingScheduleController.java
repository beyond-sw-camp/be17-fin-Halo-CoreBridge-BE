package com.halo.core_bridge.api.schedule.jobposting.controller;

import com.halo.core_bridge.api.schedule.jobposting.contents.SwaggerJobPostingScheduleContents;
import com.halo.core_bridge.api.schedule.jobposting.model.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.service.JobPostingScheduleService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Tag(name = "채용 공고 스케줄", description = "채용 공고 스케줄 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/jobpostings")
public class JobPostingScheduleController {

    private final JobPostingScheduleService service;

    @Operation(
            summary = "채용 공고 스케줄 생성",
            description = "새로운 채용 공고 스케줄을 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobPostingScheduleDto.Create.class),
                            examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.CREATE_REQUEST))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.CREATE_RESPONSE)
                            )
                    )

            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<JobPostingScheduleDto.Response>> create(
            @Valid @RequestBody JobPostingScheduleDto.Create req) {

        JobPostingScheduleDto.Response created = service.create(req);
        return ResponseEntity
                .created(URI.create("/api/schedules/jobpostings/" + created.getId()))
                .body(BaseResponse.success(created));
    }

    @Operation(
            summary = "채용 공고 스케줄 수정", description = "ID로 특정 채용 공고 스케줄을 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobPostingScheduleDto.Update.class),
                            examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.UPDATE_REQUEST))
            ),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "스케줄 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.UPDATE_RESPONSE)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostingScheduleDto.Response>> update(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingScheduleDto.Update req) {

        return ResponseEntity
                .ok(BaseResponse.success(service.update(id, req)));
    }

    @Operation(
            summary = "채용 공고 스케줄 삭제",
            description = "ID로 특정 채용 공고 스케줄을 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "스케줄 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204", description = "삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.DELETE_RESPONSE)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();     // BaseResponse 필요 없음
    }

    @Operation(
            summary = "채용 공고 스케줄 목록 조회",
            description = "모든 채용 공고 스케줄 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.LIST_RESPONSE)
                            )
                    )

            }
    )
    @GetMapping
    public ResponseEntity<BaseResponse<List<JobPostingScheduleDto.Response>>> list() {
        return ResponseEntity
                .ok(BaseResponse.success(service.list()));
    }

    @Operation(
            summary = "채용 공고 스케줄 상세 조회",
            description = "ID로 특정 채용 공고 스케줄을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "스케줄 ID",
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
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.GET_RESPONSE)
                            )
                    )

            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostingScheduleDto.Response>> get(@PathVariable Long id) {
        return ResponseEntity
                .ok(BaseResponse.success(service.get(id)));
    }

    @Operation(
            summary = "채용 공고 캘린더 조회",
            description = "년도와 월로 채용 공고 캘린더를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.CALENDAR_RESPONSE)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "year",
                            description = "년도",
                            required = true,
                            example = "2025"
                    ),
                    @Parameter(
                            name = "month",
                            description = "월",
                            required = true,
                            example = "10"

                    )
            }
    )
    @GetMapping("/calendar")
    public ResponseEntity<BaseResponse<Map<String, List<JobPostingScheduleDto.CalendarItem>>>> calendar(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity
                .ok(BaseResponse.success(service.calendar(year, month)));
    }

    @Operation(
            summary = "채용 공고 스케줄 공유",
            description = "ID로 특정 채용 공고 스케줄을 다른 사용자에게 공유합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobPostingScheduleDto.ShareRequest.class),
                            examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.SHARE_REQUEST))
            ),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "스케줄 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "공유 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.SHARE_RESPONSE)
                            )
                    )
            }
    )
    @PostMapping("/{id}/share")
    public ResponseEntity<BaseResponse<String>> share(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingScheduleDto.ShareRequest req) {

        log.info("scheduleId={}, userIds={}", id, req.getUserIds());

        service.share(id, req);

        return ResponseEntity.ok(BaseResponse.success("공유 완료"));
    }

    @Operation(
            summary = "채용 공고 스케줄 일괄 공유",
            description = "여러 채용 공고 스케줄을 여러 사용자에게 일괄 공유합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingScheduleDto.BulkShareRequest.class),
                            examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.BULK_SHARE_REQUEST)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일괄 공유 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerJobPostingScheduleContents.BULK_SHARE_RESPONSE)
                            )
                    )
            }
    )
    @PostMapping("/share")
    public ResponseEntity<BaseResponse<Void>> bulkShare(
            @Valid @RequestBody JobPostingScheduleDto.BulkShareRequest req) {

        service.bulkShare(req);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
