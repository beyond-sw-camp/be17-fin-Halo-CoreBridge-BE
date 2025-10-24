package com.halo.core_bridge.api.jobposting.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmploymentType {
    FULL_TIME("정규직"),
    CONTRACT("계약직"),
    INTERN("인턴");

    private final String label;

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static EmploymentType fromLabel(String label) {
        for(EmploymentType type : EmploymentType.values()) {
            if(type.label.equals(label)) {
                return type;
            }
        }
        throw BaseException.from(BaseResponseStatus.FIELD_VALIDATE_ERROR);
    }
}
