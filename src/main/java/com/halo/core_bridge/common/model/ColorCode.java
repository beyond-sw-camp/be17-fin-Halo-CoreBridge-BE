package com.halo.core_bridge.common.model;

import lombok.Getter;

@Getter
public enum ColorCode {
    BLUE("파랑", "blue-300"),
    RED("빨강", "red-300"),
    ORANGE("주황", "orange-300"),
    PURPLE("보라", "purple-300"),
    pink("분홍", "pink-300")

    private final String colorName;
    private final String colorCode;

    ColorCode(String colorName, String colorCode) {
        this.colorName = colorName;
        this.colorCode = colorCode;
    }
}
