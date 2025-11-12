package com.halo.core_bridge.api.schedule.jobprocess.service;

import com.halo.core_bridge.api.schedule.jobprocess.model.dto.JobProcessScheduleDto;
import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessSchedule;
import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessScheduleShare;
import com.halo.core_bridge.api.schedule.jobprocess.model.enums.RecurrenceType;
import com.halo.core_bridge.api.schedule.jobprocess.repository.JobProcessScheduleRepository;
import com.halo.core_bridge.api.schedule.jobprocess.repository.JobProcessScheduleShareRepository;
import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobProcessScheduleService {

    private final JobProcessScheduleRepository repository;
    private final JobProcessScheduleShareRepository shareRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /** 일정 생성 (Recurring 지원) */
    public JobProcessScheduleDto.Response create(Long jobPostingId, JobProcessScheduleDto.Create dto) {

        if (jobPostingId == null) {
            throw BaseException.from(BaseResponseStatus.JOB_POSTING_SCHEDULE_NOT_FOUND);
        }

        User assignee = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        JobProcessSchedule entity = JobProcessSchedule.from(dto, assignee);
        entity.setJobPostingId(jobPostingId);

        JobProcessSchedule saved = repository.save(entity);

        // Recurring 일정 생성
        if (dto.getRecurrenceType() != null && dto.getRecurrenceType() != RecurrenceType.NONE) {
            createRecurringSchedules(saved, dto, assignee);
        }

        notificationService.createAndDispatch(
                NotificationDto.Request.builder()
                        .userId(assignee.getId())
                        .role(UserRoleType.valueOf(assignee.getUserRole().getCode()))
                        .type(NotificationType.PROCESS_SCHEDULE_CREATED)
                        .title("새로운 프로세스 일정이 등록되었습니다.")
                        .message(dto.getTitle() + " (" + dto.getScheduleType() + ")")
                        .build()
        );

        return toDto(saved);
    }

    /** Recurring 일정 자동 생성 */
    private void createRecurringSchedules(JobProcessSchedule parent, JobProcessScheduleDto.Create dto, User assignee) {
        LocalDate currentDate = dto.getStartDate();
        LocalDate endDate = dto.getRecurrenceEndDate() != null ? dto.getRecurrenceEndDate() : dto.getStartDate().plusMonths(6);

        int interval = dto.getRecurrenceInterval() != null ? dto.getRecurrenceInterval() : 1;

        List<JobProcessSchedule> recurringSchedules = new ArrayList<>();

        while (true) {
            // 다음 일정 날짜 계산
            currentDate = calculateNextDate(currentDate, dto.getRecurrenceType(), interval);

            if (currentDate.isAfter(endDate)) {
                break;
            }

            // 새로운 일정 생성
            JobProcessSchedule recurring = JobProcessSchedule.builder()
                    .scheduleType(parent.getScheduleType())
                    .title(parent.getTitle())
                    .candidateName(parent.getCandidateName())
                    .position(parent.getPosition())
                    .startDate(currentDate)
                    .endDate(currentDate)
                    .startTime(parent.getStartTime())
                    .endTime(parent.getEndTime())
                    .location(parent.getLocation())
                    .priority(parent.getPriority())
                    .interviewer(parent.getInterviewer())
                    .notes(parent.getNotes())
                    .status(parent.getStatus())
                    .recurrenceType(RecurrenceType.NONE) // 생성된 일정은 반복하지 않음
                    .parentScheduleId(parent.getId())
                    .jobPostingId(parent.getJobPostingId())
                    .assignedTo(assignee)
                    .build();

            recurringSchedules.add(recurring);

            // 너무 많은 일정이 생성되는 것을 방지 (최대 52개, 약 1년)
            if (recurringSchedules.size() >= 52) {
                break;
            }
        }

        if (!recurringSchedules.isEmpty()) {
            repository.saveAll(recurringSchedules);
        }
    }

    /** 다음 날짜 계산 */
    private LocalDate calculateNextDate(LocalDate currentDate, RecurrenceType type, int interval) {
        return switch (type) {
            case DAILY -> currentDate.plusDays(interval);
            case WEEKLY -> currentDate.plusWeeks(interval);
            case BIWEEKLY -> currentDate.plusWeeks(2 * interval);
            case MONTHLY -> currentDate.plusMonths(interval);
            default -> currentDate;
        };
    }

    /** 일정 수정 (URL의 jobPostingId가 진실) */
    public JobProcessScheduleDto.Response update(Long scheduleId, Long jobPostingId, JobProcessScheduleDto.Update dto) {

        JobProcessSchedule schedule = repository.findByIdAndJobPostingId(scheduleId, jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        User assignee = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        schedule.update(dto, assignee);

        return toDto(schedule);
    }

    /** 전체 목록 (관리자/백오피스) */
    @Transactional(readOnly = true)
    public List<JobProcessScheduleDto.Response> list() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /** 단건 조회 (전역) */
    @Transactional(readOnly = true)
    public JobProcessScheduleDto.Response get(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));
    }

    /** 특정 공고 스케줄 목록 조회 */
    @Transactional(readOnly = true)
    public List<JobProcessScheduleDto.Response> listByPosting(Long jobPostingId) {
        return repository.findByJobPostingId(jobPostingId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /** 특정 공고 스케줄 조회 */
    @Transactional(readOnly = true)
    public JobProcessScheduleDto.Response getByPosting(Long jobPostingId, Long id) {
        return repository.findByIdAndJobPostingId(id, jobPostingId)
                .map(this::toDto)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));
    }

    /** 특정 공고 스케줄 삭제 */
    public void deleteByPosting(Long jobPostingId, Long id) {
        JobProcessSchedule schedule = repository.findByIdAndJobPostingId(id, jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        repository.delete(schedule);
    }

    /** 반복 일정 전체 삭제 */
    public void deleteRecurringSeries(Long jobPostingId, Long id) {
        JobProcessSchedule schedule = repository.findByIdAndJobPostingId(id, jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        // 원본 일정인 경우 연결된 모든 반복 일정 삭제
        if (schedule.getParentScheduleId() == null) {
            List<JobProcessSchedule> relatedSchedules = repository.findByParentScheduleId(id);
            repository.deleteAll(relatedSchedules);
        }

        repository.delete(schedule);
    }

    /** 공유 */
    @Transactional
    public void share(Long id, JobProcessScheduleDto.ShareRequest req) {
        JobProcessSchedule schedule = repository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        for (Long userId : req.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

            if (!shareRepository.existsByScheduleAndUser(schedule, user)) {
                shareRepository.save(
                        JobProcessScheduleShare.builder()
                                .schedule(schedule)
                                .user(user)
                                .build()
                );
            }

            notificationService.createAndDispatch(
                    NotificationDto.Request.builder()
                            .userId(userId)
                            .role(UserRoleType.valueOf(user.getUserRole().getCode()))
                            .type(NotificationType.JOB_PROCESS_SHARED)
                            .title("채용 프로세스 일정이 공유되었습니다.")
                            .message(schedule.getTitle())
                            .build()
            );
        }
    }

    /** 일괄 공유 */
    @Transactional
    public void bulkShare(JobProcessScheduleDto.BulkShareRequest req) {

        for (Long scheduleId : req.getSchedules()) {

            JobProcessSchedule schedule = repository.findById(scheduleId)
                    .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

            for (Long userId : req.getMembers()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

                if (!shareRepository.existsByScheduleAndUser(schedule, user)) {
                    shareRepository.save(
                            JobProcessScheduleShare.builder()
                                    .schedule(schedule)
                                    .user(user)
                                    .build()
                    );
                }

                notificationService.createAndDispatch(
                        NotificationDto.Request.builder()
                                .userId(userId)
                                .role(UserRoleType.valueOf(user.getUserRole().getCode()))
                                .type(NotificationType.JOB_PROCESS_SHARED)
                                .title("여러 채용 프로세스 일정이 공유되었습니다.")
                                .message(schedule.getTitle())
                                .build()
                );
            }
        }
    }

    /** DTO 변환 */
    private JobProcessScheduleDto.Response toDto(JobProcessSchedule e) {
        return JobProcessScheduleDto.Response.builder()
                .id(e.getId())
                .scheduleType(e.getScheduleType())
                .title(e.getTitle())
                .candidateName(e.getCandidateName())
                .position(e.getPosition())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .location(e.getLocation())
                .priority(e.getPriority())
                .interviewer(e.getInterviewer())
                .notes(e.getNotes())
                .status(e.getStatus())
                .recurrenceType(e.getRecurrenceType())
                .recurrenceInterval(e.getRecurrenceInterval())
                .recurrenceEndDate(e.getRecurrenceEndDate())
                .parentScheduleId(e.getParentScheduleId())
                .jobPostingId(e.getJobPostingId())
                .assignedTo(e.getAssignedTo().getId())
                .sharedWith(
                        e.getShares().stream()
                                .map(sh -> sh.getUser().getId())
                                .toList()
                )
                .build();
    }
}
