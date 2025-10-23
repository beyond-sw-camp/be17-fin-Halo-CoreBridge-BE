package com.halo.core_bridge.api.pdf.model.dto;


import com.halo.core_bridge.api.image.model.dto.ImageDto;
import com.halo.core_bridge.api.image.model.entity.Image;
import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import lombok.Builder;
import lombok.Getter;

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

        public static PdfDto.UploadResponseDto from(Pdf entity) {
            return PdfDto.UploadResponseDto.builder()
                    .originalName(entity.getOriginalFilename())
                    .pdfPath(entity.getSavedPath())
                    .pdfSize(entity.getFileSize())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PdfResponseDto {
        private Long id;
        private String originalFilename;
        private String savedPath;
        private String fileUrl;
        private Long fileSize;

        public static PdfDto.PdfResponseDto from(Pdf entity, String baseUrl) {
            return PdfDto.PdfResponseDto.builder()
                    .id(entity.getId())
                    .originalFilename(entity.getOriginalFilename())
                    .savedPath(entity.getSavedPath())
                    .fileUrl(baseUrl + "/PDF/" + entity.getSavedPath())
                    .fileSize(entity.getFileSize())
                    .build();
        }
    }
}
