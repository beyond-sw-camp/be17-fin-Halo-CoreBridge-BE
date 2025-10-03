package com.halo.core_bridge.api.users.controller;

import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.api.users.service.UserService;
import com.halo.core_bridge.common.model.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> login(@Valid @RequestBody UserDto.Create create) {
        userService.save(create);

        return ResponseEntity.ok(BaseResponse.success("회원 가입 성공"));
    }
}
