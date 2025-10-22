package com.halo.core_bridge.api.image.model.dto;


import com.halo.core_bridge.api.image.model.entity.Image;
import lombok.Builder;
import lombok.Getter;

public class ImageDto {

    @Getter
    @Builder
    public static class UploadResponseDto {

        private Integer idx;
        private String originalName;
        private String imageName;
        private String imagePath;
        private String imageUrl;
        private Long imageSize;

        public static UploadResponseDto from(Image entity) {
            return UploadResponseDto.builder()
                    .originalName(entity.getOriginalFilename())
                    .imagePath(entity.getSavedPath())
                    .imageSize(entity.getFileSize())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ImageResponseDto {
        private Long idx;
        private String originalFilename;
        private String savedPath;
        private Long fileSize;
        private String contentType;

        public static ImageResponseDto from(Image entity) {
            return ImageResponseDto.builder()
                    .idx(entity.getIdx())
                    .originalFilename(entity.getOriginalFilename())
                    .savedPath(entity.getSavedPath())
                    .fileSize(entity.getFileSize())
                    .contentType(entity.getContentType())
                    .build();
        }
    }
}