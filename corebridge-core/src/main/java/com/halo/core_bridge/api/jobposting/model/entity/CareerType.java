package com.halo.core_bridge.api.jobposting.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CareerType {
    NEW("신입"),
    EXPERIENCED("경력"),
    ANY("경력무관");

    private final String label;

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static CareerType fromLabel(String label) {
        for(CareerType type : CareerType.values()) {
            if(type.label.equals(label)) {
                return type;
            }
        }
        throw BaseException.from(BaseResponseStatus.FIELD_VALIDATE_ERROR);
    }
}
