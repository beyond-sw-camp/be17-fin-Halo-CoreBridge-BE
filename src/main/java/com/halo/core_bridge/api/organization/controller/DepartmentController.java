package com.halo.core_bridge.api.organization.controller;

import com.halo.core_bridge.api.organization.model.dto.DepartmentDto;
import com.halo.core_bridge.api.organization.service.DepartmentService;
import com.halo.core_bridge.common.model.BaseResponse;
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
@Tag(name = "부서관련 API", description = "부서관련 CRUD API")
public class DepartmentController {
    private final DepartmentService departmentService;

    //부서 조회
    @GetMapping
    public ResponseEntity<BaseResponse<List<DepartmentDto.Read>>> getAllDepartments() {
        List<DepartmentDto.Read> allDepartments = departmentService.getAllDepartments();
        return  ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(allDepartments));
    }
}
