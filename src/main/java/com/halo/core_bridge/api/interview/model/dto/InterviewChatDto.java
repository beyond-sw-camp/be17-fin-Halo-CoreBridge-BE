package com.halo.core_bridge.api.interview.model.dto;

import lombok.Builder;
import lombok.Getter;

public class InterviewChatDto {

    @Getter
    @Builder
    public static class ResumeLoadForInfo {

        private Long jobPostingId;
        private Long resumeId;

        public static ResumeLoadForInfo from(Long jobPostingId, Long resumeId) {

            return ResumeLoadForInfo.builder()
                    .jobPostingId(jobPostingId)
                    .resumeId(resumeId)
                    .build();
        }
    }
}
