package com.halo.core_bridge.api.schedule.jobposting.service;

import com.halo.core_bridge.api.schedule.jobposting.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.entity.JobPostingSchedule;
import com.halo.core_bridge.api.schedule.jobposting.entity.JobPostingScheduleShare;
import com.halo.core_bridge.api.schedule.jobposting.repository.JobPostingScheduleRepository;
import com.halo.core_bridge.api.schedule.jobposting.repository.JobPostingScheduleShareRepository;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPostingScheduleService {

    private final JobPostingScheduleRepository repository;
    private final JobPostingScheduleShareRepository shareRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public JobPostingScheduleDto.Response create(JobPostingScheduleDto.Create dto) {
        User assignee = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));
        JobPostingSchedule e = JobPostingSchedule.from(dto, assignee);
        JobPostingSchedule saved = repository.save(e);
        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_CREATED,
                "새로운 채용 일정이 추가되었습니다.",
                dto.getTitle(),
                "/recruiter/jobs/" + dto.getType() + "/schedule"
        );
        return JobPostingScheduleDto.toDto(saved);
    }

    public JobPostingScheduleDto.Response update(Long id, JobPostingScheduleDto.Update dto) {
        JobPostingSchedule e = repository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));
        User assignee = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        // update fields
        e.setTitle(dto.getTitle());
        e.setPosition(dto.getPosition());
        e.setDepartment(dto.getDepartment());
        e.setExperience(dto.getExperience());
        e.setType(dto.getType());
        e.setAssignedTo(assignee);
        e.setPostedDate(dto.getPostedDate());
        e.setDeadline(dto.getDeadline());
        e.setStartTime(dto.getStartTime());
        e.setEndTime(dto.getEndTime());
        e.setStatus(dto.getStatus());
        e.setDescription(dto.getDescription());
        e.setResponsibilities(dto.getResponsibilities());
        e.setRequirements(dto.getRequirements());
        e.setPreferences(dto.getPreferences());
        e.setBenefits(dto.getBenefits());
        e.setUrgent(dto.isUrgent());

        JobPostingSchedule saved = repository.save(e);
        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_UPDATED,
                "공고 일정이 수정되었습니다: " + dto.getTitle(),
                dto.getPosition(),
                "/recruiter/jobs/" + dto.getPosition() + "/schedule"
        );
        return JobPostingScheduleDto.toDto(saved);
    }

    public void delete(Long id) {
        JobPostingSchedule e = repository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));
        repository.delete(e);
        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_DELETED,
                "공고 일정이 삭제되었습니다: " + id, String.valueOf(id),
                "/recruiter/jobs/" + id + "/schedule"
        );
    }

    @Transactional(readOnly = true)
    public List<JobPostingScheduleDto.Response> list() {
        return repository.findAll().stream().map(JobPostingScheduleDto::toDto).toList();
    }

    @Transactional(readOnly = true)
    public JobPostingScheduleDto.Response get(Long id) {
        return repository.findById(id).map(JobPostingScheduleDto::toDto)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));
    }

    @Transactional
    public void share(Long id, JobPostingScheduleDto.ShareRequest req) {
        JobPostingSchedule schedule = repository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));

        for (Long uid : req.getUserIds()) {
            User user = userRepository.findById(uid)
                    .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

            // 이미 공유된 관계라면 skip
            boolean alreadyShared = shareRepository.existsByScheduleAndUser(schedule, user);
            if (alreadyShared) continue;

            JobPostingScheduleShare share = JobPostingScheduleShare.builder()
                    .schedule(schedule)
                    .user(user)
                    .build();

            shareRepository.save(share);
        }

        notificationService.publishNotification(
                1L,
                NotificationType.JOB_SCHEDULE_DELETED,
                "공고 일정이 공유되었습니다: " + req.getUserIds(), String.valueOf(id),
                "/recruiter/jobs/" + req.getUserIds() + "/schedule"
        );
    }

    @Transactional
    public void bulkShare(JobPostingScheduleDto.BulkShareRequest req) {

        for (Long jobId : req.getJobs()) {
            JobPostingSchedule schedule = repository.findById(jobId)
                    .orElseThrow(() -> BaseException.from(BaseResponseStatus.JOB_POSTING_NOT_FOUND));

            for (Long userId : req.getMembers()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

                boolean alreadyShared = shareRepository.existsByScheduleAndUser(schedule, user);
                if (alreadyShared) continue;

                JobPostingScheduleShare share = JobPostingScheduleShare.builder()
                        .schedule(schedule)
                        .user(user)
                        .build();

                shareRepository.save(share);
            }

            notificationService.publishNotification(
                    1L,
                    NotificationType.JOB_SCHEDULE_DELETED,
                    "공고 일정이 공유되었습니다: " + req.getMembers(), "dd",
                    "/recruiter/jobs/" + req.getJobs() + "/schedule"
            );
        }
    }




    @Transactional(readOnly = true)
    public Map<String, List<JobPostingScheduleDto.CalendarItem>> calendar(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<JobPostingSchedule> all = repository.findAll();
        Map<String, List<JobPostingScheduleDto.CalendarItem>> map = new LinkedHashMap<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            map.put(d.toString(), new ArrayList<>());
        }

        for (JobPostingSchedule s : all) {
            LocalDate from = s.getPostedDate().isBefore(start) ? start : s.getPostedDate();
            LocalDate to = s.getDeadline().isAfter(end) ? end : s.getDeadline();
            for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
                String key = d.toString();
                if (map.containsKey(key)) {
                    map.get(key).add(JobPostingScheduleDto.CalendarItem.builder()
                            .id(s.getId())
                            .title(s.getTitle())
                            .status(s.getStatus())
                            .urgent(s.isUrgent())
                            .department(s.getDepartment())
                            .build());
                }
            }
        }
        return map;
    }
}
