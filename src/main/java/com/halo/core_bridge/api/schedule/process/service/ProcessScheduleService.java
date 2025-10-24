package com.halo.core_bridge.api.schedule.process.service;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.repository.JobPostingRepository;
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

        return first.toDto();
    }

    @Transactional
    public ProcessScheduleDto.Response update(Long scheduleId, ProcessScheduleDto.Request request) {
        log.info("[ProcessScheduleService] update scheduleId={}, request={}", scheduleId, request);
        ProcessSchedule schedule = processScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.PROCESS_SCHEDULE_NOT_FOUND));
        schedule.update(request);

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
    }

    private JobPosting validateJobPostingExists(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));
    }
}
