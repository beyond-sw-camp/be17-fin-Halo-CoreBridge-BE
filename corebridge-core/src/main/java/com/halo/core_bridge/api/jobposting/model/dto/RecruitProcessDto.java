package com.halo.core_bridge.api.jobposting.model.dto;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.common.model.ColorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.halo.core_bridge.api.jobposting.model.document.JobPostingDocument.ProcessInfo;

public class RecruitProcessDto {

    @Getter
    public static class Create {
        @NotBlank(message = "프로세스명은 비어 있을 수 없습니다.")
        @Size(max = 10, message = "프로세스명은 10자 이하로 입력해주세요.")
        private String name;

        @NotNull(message = "색상 코드는 필수 입력 값 입니다.")
        private ColorCode color;

        @NotNull(message = "해당 프로세스의 순서 입력은 필수 값 입니다.")
        private Integer orderIdx;

        public RecruitProcess toEntity(Long jobPostingId) {
            return RecruitProcess.builder()
                    .name(this.name)
                    .colorCode(this.color)
                    .orderIdx(this.orderIdx)
                    .jobPosting(JobPosting.builder().id(jobPostingId).build())
                    .build();
        }

    }


    @Getter
    public static class Add {

        @NotBlank(message = "이름은 필수 입력 값 입니다.")
        private String name;

        @NotBlank(message = "색상 코드는 필수 입력 값 입니다.")
        private ColorCode colorCode;

        @NotBlank(message = "채용 공고 ID는 필수 입력 값 입니다.")
        private Long jobPostingId;

        public RecruitProcess toEntity(int orderIdx) {

            return RecruitProcess.builder()
                    .name(this.name)
                    .colorCode(this.colorCode)
                    .orderIdx(orderIdx)
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
        private ColorCode colorCode;
        private int orderIdx;

        public static Read from(RecruitProcess entity) {
            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .colorCode(entity.getColorCode())
                    .orderIdx(entity.getOrderIdx())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class recruitProcesses {

        private List<Read> recruitProcesses;

        public static recruitProcesses from(List<RecruitProcess> processes) {

            return RecruitProcessDto.recruitProcesses.builder()
                    .recruitProcesses(processes.stream().map(Read::from).toList())
                    .build();

        }
    }

    @Getter
    public static class ChangeOrder {
        private Long processId;
        private Long jobPostingId;
        private int fromIdx;
        private int toIdx;
    }

    @Getter
    public static class Update {
        private String name;
        private ColorCode colorCode;
        private Long jobPostingId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProcessSummary {
        private String stageName;
        private Long count;
        private Integer orderIndex;

        public static ProcessSummary from(ProcessCount processCount) {

            return ProcessSummary.builder()
                    .stageName(processCount.stageName)
                    .orderIndex(processCount.orderIndex)
                    .count(processCount.count)
                    .build();

        }

        public static ProcessSummary from(ProcessInfo processInfo) {

            return ProcessSummary.builder()
                    .stageName(processInfo.getName())
                    .orderIndex(processInfo.getOrderIdx())
                    .count(processInfo.getApplicantCount())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProcessCount {
        private Long jobPostingId;
        private String stageName;
        private Integer orderIndex;
        private Long count;
    }
}
