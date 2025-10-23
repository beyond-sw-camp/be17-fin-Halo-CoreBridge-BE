package com.halo.core_bridge.api.interview.model.enums;

import lombok.Getter;

@Getter
public enum RoomType {

    OFFLINE("오프라인"),
    ONLINE("화상");

    private final  String name;
    RoomType(String name) {
        this.name = name;
    }
}
