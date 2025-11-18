package com.halo.core_bridge.api.pdf.service;

import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.pdf.model.entity.Pdf;
import com.halo.core_bridge.api.pdf.repository.PdfRepository;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.service.ResumeService;
import com.halo.core_bridge.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static com.halo.core_bridge.common.model.BaseResponseStatus.PDF_NOT_FOUND;
import static com.halo.core_bridge.common.model.BaseResponseStatus.PDF_UPLOAD_FAILED;
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
        System.out.println("===== downloadPdf 시작 =====");
        System.out.println("요청된 PDF ID: " + id);

        Pdf pdf = pdfRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    System.err.println("❌ PDF를 찾을 수 없음: ID=" + id);
                    return BaseException.from(PDF_NOT_FOUND);
                });

        System.out.println("✅ PDF 엔티티 조회 성공");
        System.out.println("원본 파일명: " + pdf.getOriginalFilename());
        System.out.println("저장 경로: " + pdf.getSavedPath());
        System.out.println("DB 파일 크기: " + pdf.getFileSize());

        String filePath = uploadPath + File.separator + pdf.getSavedPath();
        File file = new File(filePath);

        System.out.println("Upload Path: " + uploadPath);
        System.out.println("Full File Path: " + filePath);
        System.out.println("파일 존재 여부: " + file.exists());

        if (file.exists()) {
            long actualFileSize = file.length();
            System.out.println("실제 파일 크기: " + actualFileSize + " bytes");
            System.out.println("파일 읽기 가능: " + file.canRead());

            if (actualFileSize < 1000) {
                System.err.println("⚠️ 경고: 파일이 너무 작습니다 (손상 가능성)");
            }

            // 파일 헤더 확인
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] header = new byte[4];
                int read = fis.read(header);
                String headerStr = new String(header, 0, read);
                System.out.println("파일 시작 4바이트: " + headerStr);

                if (!headerStr.startsWith("%PDF")) {
                    System.err.println("❌ 이것은 PDF 파일이 아닙니다!");
                }
            } catch (Exception e) {
                System.err.println("❌ 파일 헤더 읽기 실패: " + e.getMessage());
            }
        } else {
            System.err.println("❌ 파일이 존재하지 않습니다: " + filePath);
            throw BaseException.from(PDF_NOT_FOUND);
        }

        System.out.println("===== downloadPdf 완료 =====");
        return new FileSystemResource(file);
    }

    // 헬퍼 메서드
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
