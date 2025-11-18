package com.halo.core_bridge.api.myPage.controller;

import com.halo.core_bridge.api.myPage.model.MypageDto;
import com.halo.core_bridge.api.myPage.service.MyPageService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applicant")
@RequiredArgsConstructor
public class MypageController {
    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public ResponseEntity<BaseResponse<MypageDto.MyPageResponse>> getMyPage(
            @AuthenticationPrincipal UserDto.Auth dto
    ) {
        Long userId = dto.getId(); // 로그인한 유저의 id
        MypageDto.MyPageResponse myPage = myPageService.getMyPage(userId);

        return ResponseEntity.ok(BaseResponse.success(myPage));
    }
}
