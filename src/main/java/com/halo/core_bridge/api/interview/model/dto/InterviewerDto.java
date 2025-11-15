package com.halo.core_bridge.api.interview.model.dto;

import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class InterviewerDto {

    @Getter
    @Builder
    public static class InterviewerInfo {

        private Long id;
        private String name;
        private String email;

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
