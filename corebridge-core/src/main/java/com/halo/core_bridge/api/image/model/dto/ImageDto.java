package com.halo.core_bridge.api.image.model.dto;


import com.halo.core_bridge.api.image.model.entity.Image;
import lombok.Builder;
import lombok.Getter;

public class ImageDto {

    @Getter
    @Builder
    public static class UploadResponseDto {

        private Long id;
        private String originalName;
        private String imageName;
        private String imagePath;
        private String imageUrl;
        private Long imageSize;

        public static UploadResponseDto from(Image entity) {
            return UploadResponseDto.builder()
                    .id(entity.getId())
                    .originalName(entity.getOriginalFilename())
                    .imageName(entity.getSavedPath())
                    .imagePath(entity.getSavedPath())
                    .imageSize(entity.getFileSize())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ImageResponseDto {
        private Long id;
        private String originalFilename;
        private String savedPath;
        private Long fileSize;
        private String contentType;

        public static ImageResponseDto from(Image entity) {
            return ImageResponseDto.builder()
                    .id(entity.getId())
                    .originalFilename(entity.getOriginalFilename())
                    .savedPath(entity.getSavedPath())
                    .fileSize(entity.getFileSize())
                    .contentType(entity.getContentType())
                    .build();
        }
    }
}