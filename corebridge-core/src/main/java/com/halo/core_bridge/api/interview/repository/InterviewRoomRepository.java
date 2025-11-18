package com.halo.core_bridge.api.interview.repository;

import com.halo.core_bridge.api.interview.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRoomRepository extends JpaRepository<Room, Long> {
    boolean existsRoomByLocation(String location);
}
