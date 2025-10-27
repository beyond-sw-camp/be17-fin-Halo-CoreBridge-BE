package com.halo.core_bridge.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ColorCode {

    BLUE("파랑", "blue-500"),
    RED("빨강", "red-500"),
    ORANGE("주황", "orange-500"),
    PURPLE("보라", "purple-500"),
    PINK("분홍", "pink-500");

    private final String name;
    private final String code;

    ColorCode(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
