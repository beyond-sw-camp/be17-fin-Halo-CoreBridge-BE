package com.halo.core_bridge.api.schedule.jobprocess.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecurrenceType {
    NONE("없음", "none"),
    DAILY("매일", "daily"),
    WEEKLY("매주", "weekly"),
    BIWEEKLY("격주", "biweekly"),
    MONTHLY("매월", "monthly");

    private final String description;
    private final String code;

    public static RecurrenceType fromCode(String code) {
        for (RecurrenceType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return NONE;
    }
}
