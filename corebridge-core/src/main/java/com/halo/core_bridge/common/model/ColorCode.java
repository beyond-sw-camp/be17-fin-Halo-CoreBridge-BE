package com.halo.core_bridge.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.halo.core_bridge.common.serialize.ColorCodeSerializer;
import lombok.Getter;

@Getter
@JsonSerialize(using = ColorCodeSerializer.class)
public enum ColorCode {

    BLUE("파랑", "blue-500"),
    RED("빨강", "red-500"),
    ORANGE("주황", "orange-500"),
    PURPLE("보라", "purple-500"),
    PINK("분홍", "pink-500");

    private final String label;
    private final String code;

    ColorCode(String label, String code) {
        this.label = label;
        this.code = code;
    }
}
