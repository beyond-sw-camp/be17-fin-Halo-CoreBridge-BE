package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.dto.ResumeDto;

import java.util.List;

public interface ApplicantQueryRepository {
    List<ResumeDto.ApplicantResponse> findApplicantsByJobPostingId(Long jobPostingId);
}
