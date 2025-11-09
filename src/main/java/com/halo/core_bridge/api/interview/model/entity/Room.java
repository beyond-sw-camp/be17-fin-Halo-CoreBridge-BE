package com.halo.core_bridge.api.interview.model.entity;

import com.halo.core_bridge.api.interview.model.enums.InterviewType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private Integer capacity;

    public void changeName(String newName) {
        this.name = newName;
    }

    public void changeLocation(String newLocation) {
        this.location = newLocation;
    }

    public void changeRoomType(InterviewType newInterviewType) {
        this.interviewType = newInterviewType;
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void changeCapacity(Integer newCapacity) {
        this.capacity = newCapacity;
    }
}
