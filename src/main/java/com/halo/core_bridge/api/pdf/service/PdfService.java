package com.halo.core_bridge.api.pdf.service;

import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.common.exception.BaseException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    PdfDto.UploadResponseDto uploadPdf(MultipartFile file, String directory, Long resumeId) throws BaseException;

    void deletePdf(Long id) throws BaseException;

    PdfDto.PdfResponseDto findByResumeId(Long resumeid) throws BaseException;

    Resource downloadPdf(Long id) throws BaseException;

}
