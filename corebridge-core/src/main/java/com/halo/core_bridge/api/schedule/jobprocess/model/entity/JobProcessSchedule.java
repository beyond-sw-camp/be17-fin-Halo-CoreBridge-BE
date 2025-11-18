package com.halo.core_bridge.api.schedule.jobprocess.model.entity;

import com.halo.core_bridge.api.schedule.jobprocess.model.dto.JobProcessScheduleDto;
import com.halo.core_bridge.api.schedule.jobprocess.model.enums.RecurrenceType;
import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "job_process_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobProcessSchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 일정 기본 정보 */
    @Column(nullable = false)
    private String scheduleType;      // document_review, interview_1, interview_2 ...

    @Column(nullable = false)
    private String title;

    private String candidateName;
    private String position;

    /** 일정 기간 + 시간 */
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    /** 장소 / 메모 / 우선순위 / 상태 */
    private String location;

    @Column(nullable = false)
    private String priority; // high, medium, low

    private String interviewer;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private String status; // scheduled, completed, cancelled

    /** Recurring 기능 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RecurrenceType recurrenceType = RecurrenceType.NONE;

    private Integer recurrenceInterval; // 반복 간격 (예: 2주마다 = 2)

    private LocalDate recurrenceEndDate; // 반복 종료일

    private Long parentScheduleId; // 반복 일정의 원본 ID (자동 생성된 일정만 가짐)

    /** 공고 정보 + 담당자 */
    @Column(nullable = false)
    private Long jobPostingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private User assignedTo;

    /** 공유 엔티티 */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<JobProcessScheduleShare> shares = new LinkedHashSet<>();


    /** 생성 메서드 */
    public static JobProcessSchedule from(JobProcessScheduleDto.Create dto, User assigned) {
        return JobProcessSchedule.builder()
                .scheduleType(dto.getScheduleType())
                .title(dto.getTitle())
                .candidateName(dto.getCandidateName())
                .position(dto.getPosition())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .location(dto.getLocation())
                .priority(dto.getPriority())
                .interviewer(dto.getInterviewer())
                .notes(dto.getNotes())
                .status(dto.getStatus())
                .recurrenceType(dto.getRecurrenceType() != null ? dto.getRecurrenceType() : RecurrenceType.NONE)
                .recurrenceInterval(dto.getRecurrenceInterval())
                .recurrenceEndDate(dto.getRecurrenceEndDate())
                .assignedTo(assigned)
                .build();
    }

    /** 업데이트 메서드 */
    public void update(JobProcessScheduleDto.Update dto, User assigned) {
        this.scheduleType = dto.getScheduleType();
        this.title = dto.getTitle();
        this.candidateName = dto.getCandidateName();
        this.position = dto.getPosition();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.location = dto.getLocation();
        this.priority = dto.getPriority();
        this.interviewer = dto.getInterviewer();
        this.notes = dto.getNotes();
        this.status = dto.getStatus();
        this.recurrenceType = dto.getRecurrenceType() != null ? dto.getRecurrenceType() : this.recurrenceType;
        this.recurrenceInterval = dto.getRecurrenceInterval() != null ? dto.getRecurrenceInterval() : this.recurrenceInterval;
        this.recurrenceEndDate = dto.getRecurrenceEndDate() != null ? dto.getRecurrenceEndDate() : this.recurrenceEndDate;
        this.assignedTo = assigned;
    }
}
