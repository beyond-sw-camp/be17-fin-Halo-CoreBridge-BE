package com.halo.core_bridge.api.schedule.process.service;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import com.halo.core_bridge.api.schedule.process.model.dto.ProcessScheduleDto;
import com.halo.core_bridge.api.schedule.process.model.entity.ProcessSchedule;
import com.halo.core_bridge.api.schedule.process.repository.ProcessScheduleRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProcessScheduleService {
    private final ProcessScheduleRepository processScheduleRepository;
    private final JobPostingRepository jobPostingRepository;
    private final NotificationService notificationService;

    public List<ProcessScheduleDto.Response> getAll() {
        log.info("[ProcessScheduleService] getAll schedules");
        return processScheduleRepository.findAll().stream().map(ProcessSchedule::toDto).toList();
    }

    public List<ProcessScheduleDto.Response> getByJobPosting(Long jobPostingId) {
        log.info("[ProcessScheduleService] getByJobPosting jobPostingId={}", jobPostingId);
        validateJobPostingExists(jobPostingId);
        return processScheduleRepository.findByJobPostingId(jobPostingId).stream().map(ProcessSchedule::toDto).toList();
    }

    public ProcessScheduleDto.Response get(Long scheduleId) {
        log.info("[ProcessScheduleService] get scheduleId={}", scheduleId);
        ProcessSchedule schedule = processScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.PROCESS_SCHEDULE_NOT_FOUND));
        return schedule.toDto();
    }

    @Transactional
    public ProcessScheduleDto.Response create(ProcessScheduleDto.Request request) {
        log.info("[ProcessScheduleService] create request={}", request);
        JobPosting jobPosting = validateJobPostingExists(request.getJobPostingId());

        ProcessSchedule first = null;
        if (!request.isRecurring() || request.getStartDate().equals(request.getEndDate())) {
            first = processScheduleRepository.save(ProcessSchedule.fromRequest(request, jobPosting, null, null));
        } else {
            String groupId = UUID.randomUUID().toString();
            LocalDate start = LocalDate.parse(request.getStartDate());
            LocalDate end = LocalDate.parse(request.getEndDate());
            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                ProcessSchedule ps = processScheduleRepository.save(ProcessSchedule.fromRequest(request, jobPosting, groupId, d.toString()));
                if (first == null) first = ps;
            }
        }

        notificationService.publishNotification(
                1L,
                NotificationType.PROCESS_SCHEDULE_CREATED,
                "채용 프로세스 단계가 생성되었습니다.",
                jobPosting.getTitle(),
                "/recruiter/jobs/" + jobPosting.getId() + "/schedule"
        );
        return first.toDto();
    }

    @Transactional
    public ProcessScheduleDto.Response update(Long scheduleId, ProcessScheduleDto.Request request) {
        log.info("[ProcessScheduleService] update scheduleId={}, request={}", scheduleId, request);
        ProcessSchedule schedule = processScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.PROCESS_SCHEDULE_NOT_FOUND));
        schedule.update(request);

        notificationService.publishNotification(
                1L,
                NotificationType.PROCESS_SCHEDULE_UPDATED,
                "프로세스 일정이 수정되었습니다: " + schedule.getTitle(),
                schedule.getJobPosting().getTitle(),
                "/recruiter/jobs/" + schedule.getJobPosting().getId() + "/schedule"
        );
        return schedule.toDto();
    }

    @Transactional
    public void delete(Long scheduleId) {
        log.info("[ProcessScheduleService] delete scheduleId={}", scheduleId);
        ProcessSchedule schedule = processScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.PROCESS_SCHEDULE_NOT_FOUND));
        if (schedule.getRecurringGroupId() != null) {
            processScheduleRepository.deleteByRecurringGroupId(schedule.getRecurringGroupId());
        } else {
            processScheduleRepository.delete(schedule);
        }

        notificationService.publishNotification(
                1L,
                NotificationType.PROCESS_SCHEDULE_DELETED,
                "프로세스 일정이 삭제되었습니다: " + schedule.getTitle(),
                schedule.getJobPosting().getTitle(),
                "/recruiter/jobs/" + schedule.getJobPosting().getId() + "/schedule"
        );
    }

    private JobPosting validateJobPostingExists(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));
    }
}
