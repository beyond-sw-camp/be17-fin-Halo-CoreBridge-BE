package com.halo.core_bridge.api.jobposting.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.common.model.ColorCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RecruitProcessDto {

    @Getter
    public static class Create {

        @NotBlank(message = "이름은 필수 입력 값 입니다.")
        private String name;

        @NotBlank(message = "색상 코드는 필수 입력 값 입니다.")
        private ColorCode colorCode;

        @NotBlank(message = "채용 공고 ID는 필수 입력 값 입니다.")
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

    @Getter
    @Builder
    public static class Read {

        private Long id;
        private String name;
        private String colorCode;

        public static RecruitProcessDto.Read from(RecruitProcess entity) {
            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .colorCode(entity.getColorCode().getColorCode())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class recruitProcesses {

        private List<Read> recruitProcesses;

        public static recruitProcesses from(List<RecruitProcess> processes) {

            return RecruitProcessDto.recruitProcesses.builder()
                    .recruitProcesses(processes.stream().map(RecruitProcessDto.Read::from).toList())
                    .build();

        }
    }
}
