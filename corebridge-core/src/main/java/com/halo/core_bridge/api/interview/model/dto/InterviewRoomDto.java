package com.halo.core_bridge.api.interview.model.dto;

import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.model.enums.InterviewType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class InterviewRoomDto {

    @Getter
    public static class Create {

        private String name;
        private String location;
        private InterviewType interviewType;
        private Integer capacity;
        private String description;

        public Room toEntity() {
            return Room.builder()
                    .name(this.name)
                    .location(this.location)
                    .interviewType(this.interviewType)
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
        private InterviewType interviewType;
        private Integer capacity;
        private String description;

        public static Read fromEntity(Room entity) {
            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .location(entity.getLocation())
                    .interviewType(entity.getInterviewType())
                    .capacity(entity.getCapacity())
                    .description(entity.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InterviewRoomList {

        private List<Read> interviewRooms;

        public static  InterviewRoomList fromEntity(List<Room> rooms) {

            return InterviewRoomList.builder()
                    .interviewRooms(
                            rooms.stream().map(Read::fromEntity).toList()
                    )
                    .build();
        }
    }

    @Getter
    public static class Update {

        private String name;
        private String location;
        private InterviewType interviewType;
        private Integer capacity;
        private String description;
    }
}
