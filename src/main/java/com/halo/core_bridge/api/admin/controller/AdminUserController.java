package com.halo.core_bridge.api.admin.controller;

import com.halo.core_bridge.api.admin.service.AdminUserService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.halo.core_bridge.api.admin.model.AdminDto.AccountList;
import static com.halo.core_bridge.api.admin.model.AdminDto.UserCreate;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createUser(@RequestBody UserCreate create) {

        adminUserService.save(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("계정이 추가되었습니다."));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<AccountList>> getAccounts(@RequestParam(name = "type", required = false) String roleType,
                                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                                 @RequestParam(name = "search", required = false) String keyword) {

        AccountList findAccounts = adminUserService.findAccounts(roleType, page, keyword);
        return ResponseEntity.ok(BaseResponse.success(findAccounts));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse<Object>> deleteAccount(@PathVariable Long userId) {

        adminUserService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("계정이 삭제되었습니다."));
    }
}
