package com.halo.core_bridge.api.pdf.service;

import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import com.halo.core_bridge.api.pdf.repository.PdfRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import java.io.File;
import java.io.IOException;

import static com.halo.core_bridge.common.model.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PdfService {

    private static final String PDF_DIRECTORY = "PDF";
    private final PdfRepository pdfRepository;

    @Value("${upload.path}")
    private String uploadPath;

    private final String baseUrl = "http://localhost:8080"; // 실제 서버 URL로 변경 가능

    @Transactional
    public Long upload(MultipartFile file, Long resumeId) {
        FileUploadUtils.validatePdfFile(file);

        pdfRepository.findByResumeIdAndIsDeletedFalse(resumeId).ifPresent(existingPdf -> {
            existingPdf.safeDelete();
            deleteFileFromDisk(existingPdf.getSavedPath());
        });

        String savedFileName = FileUploadUtils.generateFileName(file.getOriginalFilename());
        String savedPath = uploadPath + File.separator + PDF_DIRECTORY + File.separator + savedFileName;
        try {
            FileUploadUtils.createDirectoryIfNotExists(uploadPath + File.separator + PDF_DIRECTORY);
            file.transferTo(new File(savedPath));

            Pdf pdf = Pdf.builder()
                    .originalFilename(file.getOriginalFilename())
                    .savedPath(savedFileName)
                    .fileSize(file.getSize())
                    .resumeId(resumeId)
                    .build();

            pdfRepository.save(pdf);
            return pdf.getId();
        } catch (IOException e) {
            throw BaseException.from(PDF_UPLOAD_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public PdfDto.PdfResponseDto findPdf(Long resumeId) {
        Pdf pdf = pdfRepository.findByResumeIdAndIsDeletedFalse(resumeId)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));
        return PdfDto.PdfResponseDto.from(pdf, baseUrl);
    }

    @Transactional
    public void deletePdf(Long resumeId) {
        Pdf pdf = pdfRepository.findByResumeIdAndIsDeletedFalse(resumeId)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));
        pdf.safeDelete();
        deleteFileFromDisk(pdf.getSavedPath());
    }

    @Transactional(readOnly = true)
    public PdfDto.PdfResponseDto findPdfById(Long id) {
        Pdf pdf = pdfRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));
        return PdfDto.PdfResponseDto.from(pdf, baseUrl);
    }

    @Transactional(readOnly = true)
    public Resource download(Long id) {
        Pdf pdf = pdfRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));
        String filePath = uploadPath + File.separator + PDF_DIRECTORY + File.separator + pdf.getSavedPath();
        File file = new File(filePath);
        if (!file.exists()) {
            throw BaseException.from(PDF_NOT_FOUND);
        }
        return new FileSystemResource(file);
    }

    private void deleteFileFromDisk(String savedFileName) {
        try {
            String filePath = uploadPath + File.separator + PDF_DIRECTORY + File.separator + savedFileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("파일 삭제 실패: " + e.getMessage());
        }
    }
}