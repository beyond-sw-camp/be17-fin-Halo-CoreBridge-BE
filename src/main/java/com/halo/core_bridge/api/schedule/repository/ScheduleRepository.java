package com.halo.core_bridge.api.schedule.repository;

import com.halo.core_bridge.api.schedule.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.scheduleOwnerId = :ownerId " +
            "AND s.scheduleYear = :year " +
            "AND s.scheduleMonth = :month " +
            "ORDER BY s.scheduleDate ASC")
    List<Schedule> findSchedulesByYearAndMonth(
            @Param("ownerId") String ownerId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.scheduleOriNo = :originId AND s.scheduleId <> :excludeId")
    List<Schedule> findClonesByOriginId(@Param("originId") Long originId,
                                        @Param("excludeId") Long excludeId);
}
