package com.halo.core_bridge.api.interview.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.interview.model.enums.InterviewType;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.halo.core_bridge.api.interview.model.dto.InterviewerDto.InterviewerInfo;

public class InterviewDto {

    @Getter
    @Builder
    public static class Create {

        @NotNull(message = "면접 시작 시간은 필수입니다.")
        private LocalDate startDate;

        @NotNull(message = "면접 시작 시간은 필수입니다.")
        private LocalTime startTime;

        @Min(value = 1, message = "면접 소요 시간은 1분 이상이어야 합니다.")
        private int duration;

        private String description;

        @NotNull(message = "이력서 ID는 필수입니다.")
        private Long resumeId;

        @NotNull(message = "면접 장소는 필수입니다.")
        private String location;

        @NotNull(message = "면접 방식은 필수입니다.")
        private InterviewType interviewType;

        @NotNull(message = "채용 프로세스 ID는 필수입니다.")
        private Long recruiterProcessId;

        public Interview toEntity() {
            return Interview.builder()
                    .duration(this.duration)
                    .status(InterviewStatus.SCHEDULED)
                    .description(this.description)
                    .startDateTime(
                            LocalDateTime.of(this.startDate, this.startTime)
                    )
                    .location(this.location)
                    .interviewType(this.interviewType)
                    .resume(
                            Resume.builder().id(resumeId).build()
                    )
                    .recruitProcess(
                            RecruitProcess.builder().id(recruiterProcessId).build()
                    )
                    .build();
        }
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Read {

        private Long id;
        private String name;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;
        private int duration;
        private String process;

        @JsonIgnore
        private InterviewType interviewType;
        private String location;

        @JsonIgnore
        private InterviewStatus interviewStatus;
        private String description;
        private List<InterviewerInfo> interviewers;

        @JsonProperty("interviewType")
        public Map<String, Object> getInterviewTypeJson() {
            if (interviewType == null) return null;

            return Map.of(
                    "code", interviewType.name(),
                    "label", interviewType.getName()
            );
        }

        @JsonProperty("interviewStatus")
        public Map<String, Object> getInterviewStatusJson() {
            if (interviewStatus == null) return null;

            return Map.of(
                    "code", interviewStatus.name(),
                    "label", interviewStatus.getName()
            );
        }

        @JsonIgnore
        private Long jobPostingId;

        public static Read from(Interview entity) {

            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getResume().getUser().getName())
                    .startDateTime(entity.getStartDateTime())
                    .duration(entity.getDuration())
                    .process(entity.getRecruitProcess().getName())
                    .interviewType(entity.getInterviewType())
                    .location(entity.getLocation())
                    .interviewStatus(entity.getStatus())
                    .description(entity.getDescription())
                    .interviewers(
                            entity
                                    .getResume()
                                    .getJobPosting()
                                    .getInterviewers()
                                    .stream()
                                    .map(InterviewerInfo::from)
                                    .toList()
                    )
                    .build();
        }

    }

    @Getter
    @Builder
    public static class Interviews {
        private List<Read> interviews;
        private int currentPage;
        private int totalPages;
        private long totalElements;

        public static Interviews from(List<Interview> interviews) {
            return Interviews.builder()
                    .interviews(
                            interviews.stream().map(Read::from).toList()
                    )
                    .build();
        }

        public static Interviews fromSearch(List<Read> interviews, int currentPage, int totalPages, long totalElements) {
            return Interviews.builder()
                    .interviews(interviews)
                    .currentPage(currentPage)
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .build();
        }

    }

    @Getter
    @Builder
    public static class SearchQuery {

        private String keyword;
        private int page;
        private InterviewStatus status;
        private Long userId;

        public static SearchQuery from(int page, InterviewStatus status, String keyword) {
            return SearchQuery.builder()
                    .page(page)
                    .status(status)
                    .keyword(keyword)
                    .build();
        }

        public static SearchQuery from(int page, InterviewStatus status, String keyword, Long userId) {
            return SearchQuery.builder()
                    .page(page)
                    .status(status)
                    .keyword(keyword)
                    .userId(userId)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Reminder {

        private Long id;
        private String email;
        private String name;
        private LocalDate startDate;
        private LocalTime startTime;
        private String location;

        public static Reminder from(Interview interview) {

            return Reminder.builder()
                    .id(interview.getId())
                    .email(interview.getResume().getUser().getEmail())
                    .name(interview.getResume().getUser().getName())
                    .startDate(interview.getStartDateTime().toLocalDate())
                    .startTime(interview.getStartDateTime().toLocalTime())
                    .location(interview.getInterviewType().equals(InterviewType.ONLINE) ? "온라인" : interview.getLocation())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Cancel {

        private String cancelReason;
    }
}
