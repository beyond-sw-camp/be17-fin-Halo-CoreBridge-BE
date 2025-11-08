package com.halo.core_bridge.api.admin.controller;

import com.halo.core_bridge.api.admin.model.AdminDto;
import com.halo.core_bridge.api.admin.service.AdminUserService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createUser(@RequestBody AdminDto.UserCreate create) {

        adminUserService.save(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("계정이 추가되었습니다."));
    }
}
