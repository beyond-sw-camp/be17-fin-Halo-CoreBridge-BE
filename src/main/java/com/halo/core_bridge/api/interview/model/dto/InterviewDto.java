package com.halo.core_bridge.api.interview.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class InterviewDto {

    @Getter
    @Builder
    public static class Create {

        @NotNull(message = "면접 시작 시간은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startDateTime;

        @Min(value = 1, message = "면접 소요 시간은 1분 이상이어야 합니다.")
        @Max(value = 480, message = "면접 소요 시간은 480분 이하이어야 합니다.")
        private int duration;

        @NotNull(message = "면접 상태는 필수입니다.")
        private InterviewStatus status;

        private String description;

        @NotNull(message = "이력서 ID는 필수입니다.")
        private Long resumeId;

        @NotNull(message = "면접실 ID는 필수입니다.")
        private Long roomId;

        @NotNull(message = "채용 프로세스 ID는 필수입니다.")
        private Long recruiterProcessId;
        public Interview toEntity() {
            return Interview.builder()
                    .duration(this.duration)
                    .status(this.status)
                    .description(this.description)
                    .startDateTime(this.startDateTime)
                    .resume(
                            Resume.builder().id(resumeId).build()
                    )
                    .room(
                            Room.builder().id(roomId).build()
                    )
                    .recruitProcess(
                            RecruitProcess.builder().id(recruiterProcessId).build()
                    )
                    .build();
        }
    }
}
