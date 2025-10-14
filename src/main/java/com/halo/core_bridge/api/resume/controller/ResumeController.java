package com.halo.core_bridge.api.resume.controller;

import com.halo.core_bridge.api.resume.model.dto.ResumeDto;
import com.halo.core_bridge.api.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/applies")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<Long> createResume(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ResumeDto.Create dto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        Long resumeId = resumeService.create(dto, userId);
        return ResponseEntity.ok(resumeId);
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeDto.Response> getResume(@PathVariable Long resumeId) {
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
    public ResponseEntity<List<ResumeDto.Response>> listResumes() {
        List<ResumeDto.Response> resumes = resumeService.list();
        return ResponseEntity.ok(resumes);
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {
        resumeService.delete(resumeId);
        return ResponseEntity.ok().build();
    }
}
