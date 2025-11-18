package com.halo.core_bridge.api.image.service;


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

import static com.halo.core_bridge.common.model.BaseResponseStatus.IMAGE_NOT_FOUND;
import static com.halo.core_bridge.common.model.BaseResponseStatus.IMAGE_UPLOAD_FAILED;
import static com.halo.core_bridge.utils.FileUploadUtils.*;

@Service
@RequiredArgsConstructor
public class LocalImageService implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public ImageDto.UploadResponseDto uploadImage(MultipartFile file, String directory) throws BaseException {

        validateImage(file);

        String imageName = generateFileName(file.getOriginalFilename());
        String imagePath = uploadPath + "/" + directory + "/" + imageName;
        String savedPath = directory + "/" + imageName;

        try {
            createDirectoryIfNotExists(uploadPath + "/" + directory);
            file.transferTo(new File(imagePath));

            Image entity = Image.builder()
                    .originalFilename(file.getOriginalFilename())
                    .savedPath(savedPath)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            imageRepository.save(entity);

            return ImageDto.UploadResponseDto.builder()
                    .id(entity.getId())
                    .originalName(entity.getOriginalFilename())
                    .imageName(imageName)
                    .imagePath(savedPath)
                    .imageSize(entity.getFileSize())
                    .build();

        } catch (IOException e) {
            throw BaseException.from(IMAGE_UPLOAD_FAILED);
        }
    }

    @Override
    public String find(Long idx) throws BaseException {
        Image image = imageRepository.findById(idx)
                .orElseThrow(() -> BaseException.from(IMAGE_NOT_FOUND));
        return image.getSavedPath();
    }

    @Transactional
    @Override
    public void deleteImage(Long idx) throws BaseException {
        Image image = imageRepository.findById(idx)
                .orElseThrow(() -> BaseException.from(IMAGE_NOT_FOUND));
        image.safeDelete();
    }
}