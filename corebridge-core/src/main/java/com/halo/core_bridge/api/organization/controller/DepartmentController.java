package com.halo.core_bridge.api.organization.controller;

import com.halo.core_bridge.api.organization.contents.SwaggerDepartmentContents;
import com.halo.core_bridge.api.organization.model.dto.DepartmentDto;
import com.halo.core_bridge.api.organization.service.DepartmentService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department")
@Tag(name = "부서", description = "부서 조회 API")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Operation(summary = "부서 목록 조회", description = "모든 부서 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(value = SwaggerDepartmentContents.GET_DEPARTMENT_LIST_RESPONSE)))
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<DepartmentDto.Read>>> getAllDepartments() {
        List<DepartmentDto.Read> allDepartments = departmentService.getAllDepartments();
        return  ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(allDepartments));
    }
}
