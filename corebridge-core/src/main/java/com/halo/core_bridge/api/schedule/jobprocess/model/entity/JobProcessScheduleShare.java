package com.halo.core_bridge.api.schedule.jobprocess.model.entity;

import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_process_schedule_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobProcessScheduleShare {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private JobProcessSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
