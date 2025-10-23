package com.halo.core_bridge.api.image.service;

import com.halo.core_bridge.utils.FileUploadUtils;
import com.halo.core_bridge.api.image.model.dto.ImageDto;
import com.halo.core_bridge.api.image.model.entity.Image;
import com.halo.core_bridge.api.image.repository.ImageRepository;
import com.halo.core_bridge.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

import static com.halo.core_bridge.common.model.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final String IMAGE_DIRECTORY = "profile";

    private final ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public Long uploadImage(MultipartFile file, Long userIdx) {
        // 파일 유효성 검사
        FileUploadUtils.validateImageFile(file);

        // 기존 이력서가 있다면 삭제 처리
        imageRepository.findByUserIdx(userIdx)
                .ifPresent(existingImage -> {
                    existingImage.safeDelete();
                    deleteFileFromDisk(existingImage.getSavedPath());
                });

        // 파일명 생성 및 저장 경로 설정
        String savedFileName = FileUploadUtils.generateFileName(file.getOriginalFilename());
        String savedPath = uploadPath + File.separator + IMAGE_DIRECTORY + File.separator + savedFileName;

        try {
            // 디렉토리 생성
            FileUploadUtils.createDirectoryIfNotExists(uploadPath + File.separator + IMAGE_DIRECTORY);

            // 파일 저장
            file.transferTo(new File(savedPath));

            // DB에 정보 저장
            Image image = Image.builder()
                    .originalFilename(file.getOriginalFilename())
                    .savedPath(savedFileName)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .userIdx(userIdx)
                    .build();

            imageRepository.save(image);

            return image.getIdx();

        } catch (IOException e) {
            throw BaseException.from(IMAGE_UPLOAD_FAILED);
        }
    }

    // 이미지 조회
    @Transactional(readOnly = true)
    public ImageDto.ImageResponseDto findImage(Long userIdx) {
        Image image = imageRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> BaseException.from(IMAGE_NOT_FOUND));

        return ImageDto.ImageResponseDto.from(image);
    }

    /**
     * 이력서 이미지 삭제
     */
    @Transactional
    public void deleteImage(Long userIdx) {
        Image image = imageRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> BaseException.from(IMAGE_NOT_FOUND));

        image.safeDelete();
        deleteFileFromDisk(image.getSavedPath());
    }

    private void deleteFileFromDisk(String savedFileName) {
        try {
            String filePath = uploadPath + File.separator + IMAGE_DIRECTORY + File.separator + savedFileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // 파일 삭제 실패는 로그만 남기고 계속 진행
            System.err.println("파일 삭제 실패: " + e.getMessage());
        }
    }
}
