package com.halo.core_bridge.api.interview.model.dto;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class InterviewDto {

    @Getter
    @Builder
    public static class Create {

        private LocalDateTime startDateTime;
        private int duration;
        private InterviewStatus status;
        private String description;
        private Long resumeId;
        private Long roomId;
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
