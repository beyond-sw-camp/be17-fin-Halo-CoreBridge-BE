package com.halo.core_bridge.api.schedule.process.model.entity;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.schedule.process.model.dto.ProcessScheduleDto;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessSchedule extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    private String type;
    private String title;
    private String candidateName;
    private String date; // yyyy-MM-dd
    private String location;
    @Column(length = 2000) private String notes;
    private String recurringGroupId;

    public static ProcessSchedule fromRequest(ProcessScheduleDto.Request dto, JobPosting jp, String groupId, String date) {
        return ProcessSchedule.builder()
                .jobPosting(jp)
                .type(dto.getType())
                .title(dto.getTitle())
                .candidateName(dto.getCandidateName())
                .date(date != null ? date : dto.getStartDate())
                .location(dto.getLocation())
                .notes(dto.getNotes())
                .recurringGroupId(groupId)
                .build();
    }

    public void update(ProcessScheduleDto.Request dto) {
        if (dto.getType() != null) this.type = dto.getType();
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getCandidateName() != null) this.candidateName = dto.getCandidateName();
        if (dto.getStartDate() != null) this.date = dto.getStartDate();
        if (dto.getLocation() != null) this.location = dto.getLocation();
        if (dto.getNotes() != null) this.notes = dto.getNotes();
    }

    public ProcessScheduleDto.Response toDto() {
        ProcessScheduleDto.Response r = new ProcessScheduleDto.Response();
        r.setId(this.id);
        r.setJobPostingId(this.jobPosting != null ? this.jobPosting.getId() : null);
        r.setType(this.type);
        r.setTitle(this.title);
        r.setCandidateName(this.candidateName);
        r.setDate(this.date);
        r.setLocation(this.location);
        r.setNotes(this.notes);
        r.setRecurringGroupId(this.recurringGroupId);
        return r;
    }
}
