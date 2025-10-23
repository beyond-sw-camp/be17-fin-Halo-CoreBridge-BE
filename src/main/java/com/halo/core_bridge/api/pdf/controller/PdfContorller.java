package com.halo.core_bridge.api.pdf.controller;

import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.pdf.service.PdfService;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfContorller {

    private final PdfService pdfService;



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadPdf(
            @RequestPart("file") MultipartFile file,
            @RequestParam("resumeId") Long resumeId
    ) {
        Long pdfId = pdfService.upload(file, resumeId);
        return ResponseEntity.ok(BaseResponse.success(pdfId));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PdfDto.PdfResponseDto>> getPdf(
            @RequestParam("resumeId") Long resumeId
    ) {
        PdfDto.PdfResponseDto response = pdfService.findPdf(resumeId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePdf(@RequestParam("resumeId") Long resumeId) {
        pdfService.deletePdf(resumeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        PdfDto.PdfResponseDto pdf = pdfService.findPdfById(id);
        Resource resource = pdfService.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdf.getOriginalFilename() + "\"")
                .body(resource);
    }
}