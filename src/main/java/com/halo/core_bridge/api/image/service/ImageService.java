package com.halo.core_bridge.api.image.service;

import com.halo.core_bridge.api.image.model.dto.ImageDto;
import com.halo.core_bridge.common.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ImageDto.UploadResponseDto uploadImage(MultipartFile file, String directory) throws BaseException;

    void deleteImage(Long idx) throws BaseException;

    String find(Long idx) throws BaseException;
}