package com.halo.core_bridge.api.interview.model.dto;

import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.model.enums.RoomType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class InterviewRoomDto {

    @Getter
    public static class Create {

        private String name;
        private String location;
        private RoomType roomType;
        private Integer capacity;
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

    @Getter
    @Builder
    public static class Read {

        private Long id;
        private String name;
        private String location;
        private RoomType roomType;
        private Integer capacity;
        private String description;

        public static Read fromEntity(Room entity) {
            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .location(entity.getLocation())
                    .roomType(entity.getRoomType())
                    .capacity(entity.getCapacity())
                    .description(entity.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InterviewRoomList {

        private List<InterviewRoomDto.Read> interviewRooms;

        public static  InterviewRoomList fromEntity(List<Room> rooms) {

            return InterviewRoomDto.InterviewRoomList.builder()
                    .interviewRooms(
                            rooms.stream().map(InterviewRoomDto.Read::fromEntity).toList()
                    )
                    .build();
        }
    }

    @Getter
    public static class Update {

        private String name;
        private String location;
        private RoomType roomType;
        private Integer capacity;
        private String description;
    }
}
