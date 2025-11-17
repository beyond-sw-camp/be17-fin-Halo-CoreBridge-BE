package com.halo.core_bridge.api.interview.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import lombok.*;

import java.util.List;

public class InterviewerDto {

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InterviewerInfo {

        private Long id;
        private String name;
        private String email;

        @JsonIgnore
        private Long interviewId;

        @JsonIgnore
        private Long jobPostingId;

        public static InterviewerInfo from(Interviewer entity) {
            return InterviewerInfo.builder()
                    .id(entity.getId())
                    .name(entity.getUser().getName())
                    .email(entity.getUser().getEmail())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InterviewerList {

        private List<InterviewerInfo> interviewers;

        public static InterviewerList from(List<Interviewer> interviewers) {

            return InterviewerList.builder()
                    .interviewers(
                            interviewers.stream().map(InterviewerInfo::from).toList()
                    )
                    .build();
        }
    }
}
