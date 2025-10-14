package com.halo.core_bridge.api.schedule.model.dto;

import com.halo.core_bridge.api.schedule.model.entity.Schedule;
import lombok.*;

import java.util.List;

public class ScheduleDto {

    /** 일정 등록 */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Registration {
        private Long id;
        private Long oriId;
        @Setter private String ownerId;
        @Setter private String oriOwnerId;
        private Integer year;
        private Integer month;
        private Integer date;
        private String title;
        private String body;

        public Schedule toEntity() {
            return Schedule.builder()
                    .scheduleId(id)
                    .scheduleOriNo(oriId)
                    .scheduleOwnerId(ownerId)
                    .scheduleOriOwnerId(oriOwnerId)
                    .scheduleYear(year)
                    .scheduleMonth(month)
                    .scheduleDate(date)
                    .scheduleTitle(title)
                    .scheduleBody(body)
                    .build();
        }
    }

    /** 일정 수정 */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String title;
        private String body;
        private Integer year;
        private Integer month;
        private Integer date;
    }

    /** 일정 조회 응답 */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long oriId;
        private String ownerId;
        private String oriOwnerId;
        private Integer year;
        private Integer month;
        private Integer date;
        private String title;
        private String body;
        private String createdAt;
        private String updatedAt;

        public static Response fromEntity(Schedule s) {
            return Response.builder()
                    .id(s.getScheduleId())
                    .oriId(s.getScheduleOriNo())
                    .ownerId(s.getScheduleOwnerId())
                    .oriOwnerId(s.getScheduleOriOwnerId())
                    .year(s.getScheduleYear())
                    .month(s.getScheduleMonth())
                    .date(s.getScheduleDate())
                    .title(s.getScheduleTitle())
                    .body(s.getScheduleBody())
                    .createdAt(s.getCreatedAt() != null ? s.getCreatedAt().toString() : null)
                    .updatedAt(s.getUpdatedAt() != null ? s.getUpdatedAt().toString() : null)
                    .build();
        }
    }

    /** 등록 결과 */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationResult {
        private Long id;
        public static RegistrationResult of(Long id) {
            return RegistrationResult.builder().id(id).build();
        }
    }

    /** 공유 요청 */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareRequest {
        private List<String> targetUserIds;
    }

    /** 복제 결과 */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CloneResult {
        private Long clonedId;
        private String newOwnerId;
        private String originOwnerId;
        private String message;

        public static CloneResult success(Schedule cloned) {
            return CloneResult.builder()
                    .clonedId(cloned.getScheduleId())
                    .newOwnerId(cloned.getScheduleOwnerId())
                    .originOwnerId(cloned.getScheduleOriOwnerId())
                    .message("일정 복제 성공")
                    .build();
        }
    }
}
