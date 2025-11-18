package com.halo.core_bridge.api.coverLetterTitle.model.dto;


import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor // 모든 필드를 포함하는 생성자를 자동으로 만들어줍니다.
public class CoverLetterTitleDto {

    @Getter
    @AllArgsConstructor // 생성자를 통해 jobPostingId, title, subtitle을 받습니다.
    public static class CoverLetterTitleRequest {
        private final String title;
        private final String subtitle;

        public CoverLetterTitle toEntity(Long jobpostId) {
            return CoverLetterTitle.builder()
                    .jobPostingId(jobpostId)
                    .title(this.title)
                    .subtitle(this.subtitle)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CoverLetterTitleResponse {
        private Long id;
        private String title;
        private String subtitle;
        private Long jobPostingId;

        public static CoverLetterTitleResponse from(CoverLetterTitle entity) {
            return CoverLetterTitleResponse.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .subtitle(entity.getSubtitle())
                    .jobPostingId(entity.getJobPostingId())
                    .build();
        }
    }
}