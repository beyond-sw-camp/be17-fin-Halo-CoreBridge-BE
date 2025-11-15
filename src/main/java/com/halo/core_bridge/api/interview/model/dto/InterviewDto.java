package com.halo.core_bridge.api.interview.model.dto;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.interview.model.enums.InterviewType;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class InterviewDto {

    @Getter
    @Builder
    public static class Create {

        @NotNull(message = "면접 시작 시간은 필수입니다.")
        private LocalDate startDate;

        @NotNull(message = "면접 시작 시간은 필수입니다.")
        private LocalTime startTime;

        @Min(value = 1, message = "면접 소요 시간은 1분 이상이어야 합니다.")
        private int duration;

        private String description;

        @NotNull(message = "이력서 ID는 필수입니다.")
        private Long resumeId;

        @NotNull(message = "면접 장소는 필수입니다.")
        private String location;

        @NotNull(message = "면접 방식은 필수입니다.")
        private InterviewType interviewType;

        @NotNull(message = "채용 프로세스 ID는 필수입니다.")
        private Long recruiterProcessId;

        public Interview toEntity() {
            return Interview.builder()
                    .duration(this.duration)
                    .status(InterviewStatus.SCHEDULED)
                    .description(this.description)
                    .startDateTime(
                            LocalDateTime.of(this.startDate, this.startTime)
                    )
                    .location(this.location)
                    .interviewType(this.interviewType)
                    .resume(
                            Resume.builder().id(resumeId).build()
                    )
                    .recruitProcess(
                            RecruitProcess.builder().id(recruiterProcessId).build()
                    )
                    .build();
        }
    }
}
