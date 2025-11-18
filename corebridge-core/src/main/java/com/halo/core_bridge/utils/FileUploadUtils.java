package com.halo.core_bridge.utils;

import com.halo.core_bridge.common.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.halo.core_bridge.common.model.BaseResponseStatus.INVALID_IMAGE_FILE;
import static com.halo.core_bridge.common.model.BaseResponseStatus.INVALID_PDF_FILE;

public class FileUploadUtils {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf");
    private static final List<String> ALLOWED_PDF_EXTENSIONS = Arrays.asList("pdf");
    private static final long MAX_IMAGE_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_PDF_FILE_SIZE = 100L * 1024 * 1024;


    // UUID를 포함한 고유한 파일명 생성

    public static String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }

    //파일 확장자 추출

    private static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }


    //디렉토리가 없으면 생성
    public static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }


    // 이미지 파일 유효성 검사
    public static void validateImage(MultipartFile file) {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw BaseException.from(INVALID_IMAGE_FILE);
        }

        // 파일 크기 확인
        if (file.getSize() > MAX_IMAGE_FILE_SIZE) {
            throw BaseException.from(INVALID_IMAGE_FILE);
        }

        // 파일 확장자 확인
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw BaseException.from(INVALID_IMAGE_FILE);
        }
    }

    // PDF파일 유요성 검사
    public static void validatePdfFile(MultipartFile file) {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw BaseException.from(INVALID_PDF_FILE);
        }

        // 파일 크기 확인
        if (file.getSize() > MAX_PDF_FILE_SIZE) {
            throw BaseException.from(INVALID_PDF_FILE);
        }

        // 파일 확장자 확인
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_PDF_EXTENSIONS.contains(extension.toLowerCase())) {
            throw BaseException.from(INVALID_PDF_FILE);
        }
    }
}