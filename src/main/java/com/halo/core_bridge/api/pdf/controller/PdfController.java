package com.halo.core_bridge.api.pdf.controller;

import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.pdf.service.LocalPdfService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {  // 클래스명 오타 수정: PdfContorller -> PdfController

    private final LocalPdfService pdfService;

    @PostMapping
    public ResponseEntity<BaseResponse> register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pdf_directory") String directory,
            @RequestParam("resumeId") Long resumeId,
            @AuthenticationPrincipal UserDto.Auth loginUser
    ){
        PdfDto.UploadResponseDto response = pdfService.uploadPdf(file, directory, resumeId);
        System.out.println(response);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/find/{idx}")
    public ResponseEntity<BaseResponse<PdfDto.PdfResponseDto>> getPdf(@PathVariable Long idx){
        PdfDto.PdfResponseDto result = pdfService.findByResumeId(idx);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<BaseResponse<Void>> deletePdf(@PathVariable Long idx){
        pdfService.deletePdf(idx);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @GetMapping("/download/{resumeId}")
    public ResponseEntity<Resource> download(@PathVariable Long resumeId) {
        PdfDto.PdfResponseDto pdf = pdfService.findByResumeId(resumeId);
        Resource resource = pdfService.downloadPdf(pdf.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdf.getOriginalFilename() + "\"")
                .body(resource);
    }
}