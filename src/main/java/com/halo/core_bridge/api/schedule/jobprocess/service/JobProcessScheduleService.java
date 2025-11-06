package com.halo.core_bridge.api.schedule.jobprocess.service;

import com.halo.core_bridge.api.schedule.jobprocess.model.dto.JobProcessScheduleDto;
import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessSchedule;
import com.halo.core_bridge.api.schedule.jobprocess.model.entity.JobProcessScheduleShare;
import com.halo.core_bridge.api.schedule.jobprocess.repository.JobProcessScheduleRepository;
import com.halo.core_bridge.api.schedule.jobprocess.repository.JobProcessScheduleShareRepository;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobProcessScheduleService {

    private final JobProcessScheduleRepository repository;
    private final JobProcessScheduleShareRepository shareRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /** 일정 생성 */
    public JobProcessScheduleDto.Response create(Long jobPostingId, JobProcessScheduleDto.Create dto) {

        if (jobPostingId == null) {
            throw BaseException.from(BaseResponseStatus.JOB_POSTING_SCHEDULE_NOT_FOUND);
        }

        User assignee = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        JobProcessSchedule entity = JobProcessSchedule.from(dto, assignee);
        entity.setJobPostingId(jobPostingId); // ✅ URL 값 강제 주입

        JobProcessSchedule saved = repository.save(entity);

        notificationService.publishNotification(
                dto.getAssignedTo(),
                NotificationType.JOB_PROCESS_CREATED,
                "새로운 채용 일정이 등록되었습니다.",
                dto.getTitle() + " (" + dto.getScheduleType() + ")",
                "/recruiter/schedule"
        );

        return toDto(saved);
    }

    /** 일정 수정 (URL의 jobPostingId가 진실) */
    public JobProcessScheduleDto.Response update(Long scheduleId, Long jobPostingId, JobProcessScheduleDto.Update dto) {

        // 공고ID & 스케줄ID로 검색 (보안)
        JobProcessSchedule schedule = repository.findByIdAndJobPostingId(scheduleId, jobPostingId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        User assignee = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        schedule.update(dto, assignee);

        notificationService.publishNotification(
                dto.getAssignedTo(),
                NotificationType.JOB_PROCESS_UPDATED,
                "채용 일정이 수정되었습니다.",
                dto.getTitle() + " (" + dto.getScheduleType() + ")",
                "/recruiter/schedule"
        );

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

        notificationService.publishNotification(
                schedule.getAssignedTo().getId(),
                NotificationType.JOB_PROCESS_DELETED,
                "채용 일정이 삭제되었습니다.",
                schedule.getTitle(),
                "/recruiter/schedule"
        );
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

            notificationService.publishNotification(
                    userId,
                    NotificationType.JOB_PROCESS_SHARED,
                    "채용 일정이 공유되었습니다.",
                    schedule.getTitle(),
                    "/recruiter/schedule"
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

                notificationService.publishNotification(
                        userId,
                        NotificationType.JOB_PROCESS_SHARED,
                        "여러 채용 일정이 공유되었습니다.",
                        schedule.getTitle(),
                        "/recruiter/schedule"
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

