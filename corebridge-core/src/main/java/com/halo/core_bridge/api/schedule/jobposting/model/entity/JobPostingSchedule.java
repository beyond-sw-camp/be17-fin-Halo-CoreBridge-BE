package com.halo.core_bridge.api.schedule.jobposting.model.entity;

import com.halo.core_bridge.api.schedule.jobposting.model.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.model.enums.JobPostingStatus;
import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "job_posting_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingSchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String position;

    @Column(nullable=false)
    private String department;

    @Column(nullable=false)
    private String experience;

    @Column(nullable=false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assigned_to_id", nullable=false)
    private User assignedTo;

    @Column(nullable=false)
    private LocalDate postedDate;

    @Column(nullable=false)
    private LocalDate deadline;

    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private JobPostingStatus status;

    @Column(columnDefinition="TEXT")
    private String description;
    @Column(columnDefinition="TEXT")
    private String responsibilities;
    @Column(columnDefinition="TEXT")
    private String requirements;
    @Column(columnDefinition="TEXT")
    private String preferences;
    @Column(columnDefinition="TEXT")
    private String benefits;

    @Column(nullable=false)
    private boolean urgent;

    // metrics
    @Column(nullable=false) private int applicants = 0;
    @Column(nullable=false) private int progress = 0;
    @Column(nullable=false) private int screening = 0;
    @Column(nullable=false) private int interview1 = 0;
    @Column(nullable=false) private int interview2 = 0;
    @Column(nullable=false, name="final_stage") private int finalStage = 0;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobPostingScheduleShare> shares = new LinkedHashSet<>();

    /** Entity 생성 편의 메서드 */
    public static JobPostingSchedule from(JobPostingScheduleDto.Create dto, User assigned) {
        return JobPostingSchedule.builder()
                .title(dto.getTitle())
                .position(dto.getPosition())
                .department(dto.getDepartment())
                .experience(dto.getExperience())
                .type(dto.getType())
                .assignedTo(assigned)
                .postedDate(dto.getPostedDate())
                .deadline(dto.getDeadline())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .responsibilities(dto.getResponsibilities())
                .requirements(dto.getRequirements())
                .preferences(dto.getPreferences())
                .benefits(dto.getBenefits())
                .urgent(dto.isUrgent())
                .build();
    }
}
