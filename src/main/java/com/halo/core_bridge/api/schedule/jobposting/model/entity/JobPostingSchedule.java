package com.halo.core_bridge.api.schedule.jobposting.model.entity;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.schedule.jobposting.model.dto.JobPostingScheduleDto;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingSchedule extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    private String title;
    @Column(length = 2000)
    private String description;
    private String date; // yyyy-MM-dd
    private boolean allDay;
    private String color;
    private String recurringGroupId; // nullable

    public static JobPostingSchedule fromRequest(JobPostingScheduleDto.Request dto, JobPosting jp, String color, String groupId, String date) {
        return JobPostingSchedule.builder()
                .jobPosting(jp)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .date(date != null ? date : dto.getStartDate())
                .allDay(dto.isAllDay())
                .color(color)
                .recurringGroupId(groupId)
                .build();
    }

    public void update(JobPostingScheduleDto.Request dto, String color) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.allDay = dto.isAllDay();
        if (dto.getStartDate() != null) this.date = dto.getStartDate();
        if (color != null) this.color = color;
    }

    public JobPostingScheduleDto.Response toDto() {
        JobPostingScheduleDto.Response r = new JobPostingScheduleDto.Response();
        r.setId(this.id);
        r.setJobPostingId(this.jobPosting != null ? this.jobPosting.getId() : null);
        r.setTitle(this.title);
        r.setDescription(this.description);
        r.setDate(this.date);
        r.setAllDay(this.allDay);
        r.setColor(this.color);
        r.setRecurringGroupId(this.recurringGroupId);
        return r;
    }
}
