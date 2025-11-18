package com.halo.core_bridge.api.coverLetterDescription.model.dto;

import com.halo.core_bridge.api.coverLetterDescription.model.entity.CoverLetterDescription;
import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CoverLetterDescriptionDto {

    @Getter
    @AllArgsConstructor
    public static class CoverLetterDescriptionRequest{
        @Size(max = 10000)
        private String description;
        private Long resumeId;
        private Long coverLetterTitleId;


        public CoverLetterDescription toEntity(CoverLetterTitle title, Resume resume) {
            return CoverLetterDescription.builder()
                    .description(this.description)
                    .resume(resume)
                    .coverLetterTitle(title)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CoverLetterDescriptionResponse{
        private Long id;
        @Size(max = 10000)
        private String description;
        private Long resumeId;
        private Long coverLetterId;
        private String coverLetterTitle;

        public static CoverLetterDescriptionResponse from(CoverLetterDescription entity) {
            return CoverLetterDescriptionResponse.builder()
                    .id(entity.getId())
                    .description(entity.getDescription())
                    .resumeId(entity.getResume().getId())
                    .coverLetterId(entity.getCoverLetterTitle().getId())
                    .coverLetterTitle(entity.getCoverLetterTitle().getTitle())
                    .build();

        }
    }
}

