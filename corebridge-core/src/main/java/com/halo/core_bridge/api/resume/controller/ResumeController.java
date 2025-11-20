package com.halo.core_bridge.api.resume.controller;

import com.halo.core_bridge.api.coverLetterDescription.model.dto.CoverLetterDescriptionDto;
import com.halo.core_bridge.api.coverLetterDescription.service.CoverLetterDescriptionService;
import com.halo.core_bridge.api.coverLetterTitle.model.dto.CoverLetterTitleDto;
import com.halo.core_bridge.api.coverLetterTitle.service.CoverLetterTitleService;
import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.model.dto.ResumeSearchDto;
import com.halo.core_bridge.api.resume.service.ResumeSearchService;
import com.halo.core_bridge.api.resume.service.ResumeService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "이력서", description = "이력서 및 자기소개서 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/jobposts/{jobpostId}/applies")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final CoverLetterDescriptionService coverLetterDescriptionService;
    private final CoverLetterTitleService coverLetterTitleService;
    private final ResumeSearchService resumeSearchService;

    @PostMapping
    public ResponseEntity<Long> createResume(
            @AuthenticationPrincipal UserDto.Auth auth ,
            @RequestPart("resume") ResumeDto.Create dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        Long userId = auth.getId();
        Long resumeId = resumeService.create(dto, userId, file);


        return ResponseEntity.ok(resumeId);
    }

    @PostMapping("/search")
    public ResponseEntity<BaseResponse<ResumeSearchDto.PageResponse>> searchResumes(
            @PathVariable Long jobpostId,
            @RequestBody ResumeSearchDto.SearchRequest request) {
        log.info("Resume search request for jobpost {}: {}", jobpostId, request);

        // jobpostId를 request에 자동으로 설정
        request.setJobPostingId(jobpostId);

        ResumeSearchDto.PageResponse response = resumeSearchService.search(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }


    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeDto.Response> getResume(@PathVariable Long resumeId, @AuthenticationPrincipal UserDto.Auth auth) {
        ResumeDto.Response resume = resumeService.read(resumeId);
        return ResponseEntity.ok(resume);
    }

    @PatchMapping("/{resumeId}")
    public ResponseEntity<Void> updateResume(
            @PathVariable Long resumeId,
            @RequestBody ResumeDto.Update dto) {
        resumeService.update(resumeId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ResumeDto.ApplicantResponse>>> listResumes(@PathVariable Long jobpostId) {
        List<ResumeDto.ApplicantResponse> result = resumeService.getApplicants(jobpostId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {
        resumeService.delete(resumeId);
        return ResponseEntity.ok().build();
    }



    //-------------------------------------------------------------------------------------------------------------------

    @PostMapping("/{resumeId}/cover-letter-descriptions")
    public ResponseEntity<List<Long>> createDescriptions(
            @PathVariable Long resumeId,
            @RequestBody List<CoverLetterDescriptionDto.CoverLetterDescriptionRequest> dtoList) {

        List<Long> descriptionIds = dtoList.stream()
                .map(dto -> coverLetterDescriptionService.create(dto))
                .collect(Collectors.toList());

        return ResponseEntity.ok(descriptionIds);


    }

    @GetMapping("/{resumeId}/cover-letter-descriptions")
    public ResponseEntity<List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse>> listDescriptions(
            @PathVariable Long resumeId,
            @RequestParam Long jobPostingId) {
        List<CoverLetterDescriptionDto.CoverLetterDescriptionResponse> descriptions =
                coverLetterDescriptionService.list(resumeId, jobPostingId);
        return ResponseEntity.ok(descriptions);
    }

    @GetMapping("/cover-letter-titles")
    public ResponseEntity<List<CoverLetterTitleDto.CoverLetterTitleResponse>> getCoverLetterTitles(
            @PathVariable Long jobpostId) {
        List<CoverLetterTitleDto.CoverLetterTitleResponse> titles = coverLetterTitleService.list(jobpostId);
        return ResponseEntity.ok(titles);
    }


}