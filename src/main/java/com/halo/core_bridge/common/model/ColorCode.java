package com.halo.core_bridge.common.model;

import lombok.Getter;

@Getter
public enum ColorCode {

    BLUE("파랑", "blue-500"),
    RED("빨강", "red-500"),
    ORANGE("주황", "orange-500"),
    PURPLE("보라", "purple-500"),
    PINK("분홍", "pink-500");

    private final String colorName;
    private final String colorCode;

    ColorCode(String colorName, String colorCode) {
        this.colorName = colorName;
        this.colorCode = colorCode;
    }
}
