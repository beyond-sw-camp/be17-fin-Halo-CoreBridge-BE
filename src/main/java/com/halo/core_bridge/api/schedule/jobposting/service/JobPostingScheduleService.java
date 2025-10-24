package com.halo.core_bridge.api.schedule.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
import com.halo.core_bridge.api.schedule.jobposting.model.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingSchedule;
import com.halo.core_bridge.api.schedule.jobposting.repository.JobPostingScheduleRepository;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
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
public class JobPostingScheduleService {
    private static final String[] DEFAULT_COLORS = {"#60A5FA", "#34D399", "#F472B6", "#FBBF24", "#A78BFA", "#F87171"};
    private final JobPostingScheduleRepository jobScheduleRepository;
    private final JobPostingRepository jobPostingRepository;
    private final NotificationService notificationService;

    public List<JobPostingScheduleDto.Response> getAll() {
        log.info("[JobScheduleService] getAll");
        return jobScheduleRepository.findAll().stream().map(JobPostingSchedule::toDto).toList();
    }
    public List<JobPostingScheduleDto.Response> getByJobPosting(Long jobPostingId) {
        log.info("[JobScheduleService] getByJobPosting jobPostingId={}", jobPostingId);
        validateJobPostingExists(jobPostingId);
        return jobScheduleRepository.findByJobPostingId(jobPostingId).stream().map(JobPostingSchedule::toDto).toList();
    }
    public JobPostingScheduleDto.Response get(Long scheduleId) {
        log.info("[JobScheduleService] get scheduleId={}", scheduleId);
        JobPostingSchedule s = jobScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_SCHEDULE_NOT_FOUND));
        return s.toDto();
    }

    @Transactional
    public JobPostingScheduleDto.Response create(JobPostingScheduleDto.Request request) {
        log.info("[JobScheduleService] create request={}", request);
        JobPosting jobPosting = validateJobPostingExists(request.getJobPostingId());
        String color = resolveColor(request.getColor(), jobPosting.getId());

        JobPostingSchedule firstSaved = null;
        if (request.getStartDate().equals(request.getEndDate())) {
            firstSaved = jobScheduleRepository.save(JobPostingSchedule.fromRequest(request, jobPosting, color, null, null));
        } else {
            String groupId = UUID.randomUUID().toString();
            LocalDate start = LocalDate.parse(request.getStartDate());
            LocalDate end = LocalDate.parse(request.getEndDate());
            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                JobPostingSchedule s = jobScheduleRepository.save(JobPostingSchedule.fromRequest(request, jobPosting, color, groupId, d.toString()));
                if (firstSaved == null) firstSaved = s;
            }
        }
        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_CREATED,
                "새로운 채용 일정이 추가되었습니다.",
                jobPosting.getTitle(),
                "/recruiter/jobs/" + jobPosting.getId() + "/schedule"
        );
        return firstSaved.toDto();
    }

    @Transactional
    public JobPostingScheduleDto.Response update(Long scheduleId, JobPostingScheduleDto.Request request) {
        log.info("[JobScheduleService] update scheduleId={}, request={}", scheduleId, request);
        JobPostingSchedule schedule = jobScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_SCHEDULE_NOT_FOUND));
        String color = resolveColor(request.getColor(), schedule.getJobPosting().getId());
        schedule.update(request, color);

        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_UPDATED,
                "공고 일정이 수정되었습니다: " + schedule.getTitle(),
                schedule.getJobPosting().getTitle(),
                "/recruiter/jobs/" + schedule.getJobPosting().getId() + "/schedule"
        );
        return schedule.toDto();
    }

    @Transactional
    public void delete(Long scheduleId) {
        log.info("[JobScheduleService] delete scheduleId={}", scheduleId);
        JobPostingSchedule schedule = jobScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_SCHEDULE_NOT_FOUND));
        if (schedule.getRecurringGroupId() != null) {
            jobScheduleRepository.deleteByRecurringGroupId(schedule.getRecurringGroupId());
        } else {
            jobScheduleRepository.delete(schedule);
        }

        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_DELETED,
                "공고 일정이 삭제되었습니다: " + schedule.getTitle(),
                schedule.getJobPosting().getTitle(),
                "/recruiter/jobs/" + schedule.getJobPosting().getId() + "/schedule"
        );
    }

    private JobPosting validateJobPostingExists(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));
    }
    private String resolveColor(String inputColor, Long key) {
        if (inputColor != null && !inputColor.isBlank()) return inputColor;
        int idx = (int)(key % DEFAULT_COLORS.length);
        return DEFAULT_COLORS[idx];
    }
}
