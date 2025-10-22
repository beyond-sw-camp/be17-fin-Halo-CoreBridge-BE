package com.halo.core_bridge.api.image.Utils;

import com.halo.core_bridge.common.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.halo.core_bridge.common.model.BaseResponseStatus.*;

public class ImageUtils {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB


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


    //이력서 파일 유효성 검사
    public static void validateResumeFile(MultipartFile file) {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw BaseException.from(INVALID_IMAGE_FILE);
        }

        // 파일 크기 확인
        if (file.getSize() > MAX_FILE_SIZE) {
            throw BaseException.from(INVALID_IMAGE_FILE);
        }

        // 파일 확장자 확인
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw BaseException.from(INVALID_IMAGE_FILE);
        }
    }
}