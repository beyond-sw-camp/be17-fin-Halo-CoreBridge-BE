package com.halo.core_bridge.api.jobposting.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.common.model.ColorCode;
import lombok.Getter;

public class RecruitProcessDto {

    @Getter
    public static class Create {

        private String name;
        private ColorCode colorCode;
        private Long jobPostingId;

        public RecruitProcess toEntity() {

            return RecruitProcess.builder()
                    .name(this.name)
                    .colorCode(this.colorCode)
                    .jobPosting(
                            JobPosting.builder().id(jobPostingId).build()
                    )
                    .build();
        }
    }
}
