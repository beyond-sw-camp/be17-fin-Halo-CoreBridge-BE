package com.halo.core_bridge.api.interview.model.dto;

import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.model.enums.RoomType;
import lombok.Getter;

public class InterviewRoomDto {

    @Getter
    public static class Create {

        private String name;
        private String location;
        private RoomType roomType;
        private int capacity;
        private String description;

        public Room toEntity() {
            return Room.builder()
                    .name(this.name)
                    .location(this.location)
                    .roomType(this.roomType)
                    .capacity(this.capacity)
                    .description(this.description)
                    .build();
        }
    }
}
