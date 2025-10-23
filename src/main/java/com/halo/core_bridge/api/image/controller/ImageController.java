package com.halo.core_bridge.api.image.controller;

import com.halo.core_bridge.api.image.model.dto.ImageDto;
import com.halo.core_bridge.api.image.service.ImageService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.halo.core_bridge.api.users.model.dto.UserDto;


@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse> register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("directory") String directory,
            @AuthenticationPrincipal UserDto.Auth loginUser
    ) { Long memberIdx = loginUser.getId();
        ImageDto.UploadResponseDto response = imageService.uploadImage(file, directory);
        System.out.println(response);
        return ResponseEntity.ok(BaseResponse.success(response));
    }


    @GetMapping("/find/{idx}")
    public ResponseEntity<BaseResponse<String>> getImage(@PathVariable Long idx) {
            String result = imageService.find(idx);
            return ResponseEntity.ok(BaseResponse.success(result));
    }


    @DeleteMapping("/{idx}")
    public ResponseEntity<BaseResponse<Void>> deleteImage(@PathVariable Long idx) {
        imageService.deleteImage(idx);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}