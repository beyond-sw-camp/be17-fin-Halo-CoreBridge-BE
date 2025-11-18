package com.halo.core_bridge.api.pdf.controller;

import com.halo.core_bridge.api.pdf.contents.SwaggerPdfContents;
import com.halo.core_bridge.api.pdf.model.dto.PdfDto;
import com.halo.core_bridge.api.pdf.service.LocalPdfService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

import static com.halo.core_bridge.common.model.BaseResponseStatus.PDF_NOT_FOUND;

@Tag(name = "PDF", description = "PDF 업로드, 조회, 삭제, 다운로드 API")
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

    @Operation(
            summary = "PDF 정보 조회",
            description = "이력서 ID로 PDF 정보를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "이력서 ID",
                            description = "이력서 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerPdfContents.GET_RESPONSE)
                            )
                    )
            }
    )
    @GetMapping("/find/{idx}")
    public ResponseEntity<BaseResponse<PdfDto.PdfResponseDto>> getPdf(@PathVariable Long idx, HttpServletRequest request) {
        PdfDto.PdfResponseDto result = pdfService.findByResumeId(idx);
        String fileUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/pdf/view/" + result.getResumeId())
                .toUriString();

        result.setFileUrl(fileUrl);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(
            summary = "PDF 삭제",
            description = "PDF ID로 PDF를 삭제합니다.",
            parameters = {
                    @Parameter(
                            description = "PDF ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerPdfContents.DELETE_RESPONSE)
                            )
                    )
            }
    )
    @DeleteMapping("/{idx}")
    public ResponseEntity<BaseResponse<Void>> deletePdf(@PathVariable Long idx){
        pdfService.deletePdf(idx);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(
            summary = "PDF 다운로드",
            description = "이력서 ID로 PDF 파일을 다운로드합니다.",
            parameters = {
                    @Parameter(
                            name = "이력서 ID",
                            description = "이력서 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "다운로드 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
                    )
            }
    )
    @GetMapping("/download/{resumeId}")
    public ResponseEntity<Resource> download(@PathVariable Long resumeId) {
        PdfDto.PdfResponseDto pdf = pdfService.findByResumeId(resumeId);
        Resource resource = pdfService.downloadPdf(pdf.getId());

        String safeFilename = "resume_" + resumeId + ".pdf";

        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(resource.contentLength())  // 🔧 이 줄 추가!
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeFilename + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw BaseException.from(PDF_NOT_FOUND);
        }
    }

    @GetMapping("/view/{resumeId}")
    public ResponseEntity<Resource> viewPdf(@PathVariable Long resumeId) {
        PdfDto.PdfResponseDto pdf = pdfService.findByResumeId(resumeId);
        Resource resource = pdfService.downloadPdf(pdf.getId());

        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(resource.contentLength())  // 🔧 이 줄 추가!
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);
        } catch (IOException e) {
            throw BaseException.from(PDF_NOT_FOUND);
        }
    }
}