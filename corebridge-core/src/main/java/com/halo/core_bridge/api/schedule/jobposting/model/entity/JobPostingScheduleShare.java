package com.halo.core_bridge.api.schedule.jobposting.model.entity;

import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_posting_schedule_share",
        uniqueConstraints = @UniqueConstraint(columnNames = {"schedule_id","user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingScheduleShare {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="schedule_id", nullable=false)
    private JobPostingSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
    private User user;
}
