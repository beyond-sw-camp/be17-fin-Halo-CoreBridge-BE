package com.halo.core_bridge.api.users.model;

import lombok.Getter;

@Getter
public enum Gender {
    Male("남성"),
    Female("여성");

    private final String name;

    Gender(String name) {
        this.name = name;
    }
}