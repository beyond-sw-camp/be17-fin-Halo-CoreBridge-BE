package com.halo.core_bridge.api.interview.model.enums;

import lombok.Getter;

@Getter
public enum InterviewStatus {

    ONGOING("진행 중"),
    SCHEDULED("예정"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String name;

    InterviewStatus(String name) {
        this.name = name;
    }
}
