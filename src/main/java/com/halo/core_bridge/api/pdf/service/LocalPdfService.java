package com.halo.core_bridge.api.pdf.service;

import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import com.halo.core_bridge.api.pdf.repository.PdfRepository;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.service.ResumeService;
import com.halo.core_bridge.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import static com.halo.core_bridge.common.model.BaseResponseStatus.*;
import static com.halo.core_bridge.utils.FileUploadUtils.*;

@Service
@RequiredArgsConstructor
public class LocalPdfService implements PdfService {

    private final PdfRepository pdfRepository;
    private final ResumeService resumeService;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public PdfDto.UploadResponseDto uploadPdf(MultipartFile file, String directory, Long resumeId) throws BaseException {

        validatePdfFile(file);

        Resume resume = resumeService.findById(resumeId);

        String pdfName = generateFileName(file.getOriginalFilename());
        String pdfPath = uploadPath + File.separator + directory + File.separator + pdfName;
        String savedPath = directory + "/" + pdfName;

        try{
            createDirectoryIfNotExists(uploadPath + "/" + directory);
            file.transferTo(new File(pdfPath));

            Pdf entity = Pdf.builder()
                    .originalFilename(file.getOriginalFilename())
                    .savedPath(savedPath)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .resume(resume)
                    .build();

            pdfRepository.save(entity);

            return PdfDto.UploadResponseDto.builder()
                    .id(entity.getId())
                    .originalName(entity.getOriginalFilename())
                    .pdfName(pdfName)
                    .pdfPath(savedPath)
                    .pdfSize(entity.getFileSize())
                    .build();
        } catch (Exception e){
            throw BaseException.from(PDF_UPLOAD_FAILED);
        }
    }

    // 🔧 FIX 3: isDeleted 체크를 추가하여 삭제된 PDF는 조회되지 않도록 수정
    @Override
    @Transactional(readOnly = true)
    public PdfDto.PdfResponseDto findByResumeId(Long resumeId) throws BaseException {
        Pdf pdf = pdfRepository.findByResumeIdAndIsDeletedFalse(resumeId)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));

        return PdfDto.PdfResponseDto.builder()
                .id(pdf.getId())
                .originalFilename(pdf.getOriginalFilename())
                .savedPath(pdf.getSavedPath())
                .contentType(pdf.getContentType())
                .fileSize(pdf.getFileSize())
                .resumeId(pdf.getResume().getId())
                .build();
    }

    @Transactional
    @Override
    public void deletePdf(Long id) throws BaseException {
        Pdf pdf = pdfRepository.findById(id)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));
        pdf.safeDelete();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadPdf(Long id) {
        Pdf pdf = pdfRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> BaseException.from(PDF_NOT_FOUND));
        String filePath = uploadPath + File.separator + pdf.getSavedPath();
        File file = new File(filePath);
        if (!file.exists()) {
            throw BaseException.from(PDF_NOT_FOUND);
        }
        return new FileSystemResource(file);
    }
}