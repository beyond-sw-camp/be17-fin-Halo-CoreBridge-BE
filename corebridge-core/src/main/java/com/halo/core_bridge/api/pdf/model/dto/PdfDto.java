package com.halo.core_bridge.api.pdf.model.dto;

import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter

@Builder
public class PdfDto {

    @Getter
    @Builder
    public static class UploadResponseDto {

        private Long id;
        private String originalName;
        private String pdfName;
        private String pdfPath;
        private String pdfUrl;
        private Long pdfSize;

        public static UploadResponseDto from(Pdf entity) {
            return UploadResponseDto.builder()
                    .originalName(entity.getOriginalFilename())
                    .pdfName(entity.getOriginalFilename())
                    .pdfPath(entity.getSavedPath())
                    .pdfSize(entity.getFileSize())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class PdfResponseDto {
        private Long id;
        private String originalFilename;
        private String savedPath;
        private String contentType;
        private Long fileSize;
        private Long resumeId;
        private String fileUrl;

        public static PdfResponseDto from(Pdf entity) {
            if (entity == null) {
                return null;
            }
            return PdfResponseDto.builder()
                    .id(entity.getId())
                    .originalFilename(entity.getOriginalFilename())
                    .savedPath(entity.getSavedPath())
                    .fileSize(entity.getFileSize())
                    .contentType(entity.getContentType())
                    .resumeId(entity.getResume().getId())
                    .fileUrl("/" + entity.getSavedPath())
                    .build();
        }
    }
}