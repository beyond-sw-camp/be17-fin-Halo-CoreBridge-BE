package com.halo.core_bridge.api.image.controller;

import com.halo.core_bridge.api.image.model.dto.ImageDto;
import com.halo.core_bridge.api.image.service.ImageService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<BaseResponse<Long>> uploadResumeImage(
//            @RequestPart("file") MultipartFile file,
//            @AuthenticationPrincipal UserDto.Auth loginUser
//    ) {
//        Long imageIdx = imageService.uploadResumeImage(file, loginUser.getId());
//        return ResponseEntity.ok(BaseResponse.success(imageIdx));
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<Long>> uploadResumeImage(
            @RequestPart("file") MultipartFile file,  // RequestPart로 변경
            @RequestParam("userIdx") Long userIdx
    ) {
        Long imageIdx = imageService.uploadImage(file, userIdx);
        return ResponseEntity.ok(BaseResponse.success(imageIdx));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<ImageDto.ImageResponseDto>> getResumeImage(
            @AuthenticationPrincipal UserDto.Auth loginUser
    ) {
        ImageDto.ImageResponseDto response = imageService.findImage(loginUser.getId());
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteResumeImage(
            @AuthenticationPrincipal UserDto.Auth loginUser
    ) {
        imageService.deleteImage(loginUser.getId());
        return ResponseEntity.noContent().build();
    }
}