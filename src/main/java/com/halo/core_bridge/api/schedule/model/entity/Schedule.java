package com.halo.core_bridge.api.schedule.model.entity;

import com.halo.core_bridge.api.schedule.model.dto.ScheduleDto;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "schedule_ori_no")
    private Long scheduleOriNo;

    @Column(name = "schedule_owner_id", nullable = false)
    private String scheduleOwnerId;

    @Column(name = "schedule_ori_owner_id", nullable = false)
    private String scheduleOriOwnerId;

    @Column(name = "schedule_year")
    private Integer scheduleYear;

    @Column(name = "schedule_month")
    private Integer scheduleMonth;

    @Column(name = "schedule_date")
    private Integer scheduleDate;

    @Column(name = "schedule_title", length = 30)
    private String scheduleTitle;

    @Column(name = "schedule_body")
    private String scheduleBody;

    /** 원본 일정 번호 지정 */
    public void assignOriginalNo(Long oriNo) {
        this.scheduleOriNo = oriNo;
    }

    /** 일정 수정 */
    public void updatePartial(ScheduleDto.Update dto) {
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            this.scheduleTitle = dto.getTitle();
        }
        if (dto.getBody() != null && !dto.getBody().isBlank()) {
            this.scheduleBody = dto.getBody();
        }
        if (dto.getYear() != null) this.scheduleYear = dto.getYear();
        if (dto.getMonth() != null) this.scheduleMonth = dto.getMonth();
        if (dto.getDate() != null) this.scheduleDate = dto.getDate();
    }

    /** 복제본 내용 원본 기준으로 동기화 */
    public void updateByOrigin(Schedule origin) {
        this.scheduleTitle = origin.getScheduleTitle();
        this.scheduleBody = origin.getScheduleBody();
        this.scheduleYear = origin.getScheduleYear();
        this.scheduleMonth = origin.getScheduleMonth();
        this.scheduleDate = origin.getScheduleDate();
    }
}
