package com.halo.core_bridge.api.schedule.jobposting.service;

import com.halo.core_bridge.api.schedule.jobposting.model.dto.JobPostingScheduleDto;
import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingSchedule;
import com.halo.core_bridge.api.schedule.jobposting.model.entity.JobPostingScheduleShare;
import com.halo.core_bridge.api.schedule.jobposting.repository.JobPostingScheduleRepository;
import com.halo.core_bridge.api.schedule.jobposting.repository.JobPostingScheduleShareRepository;
import com.halo.core_bridge.api.schedule.notification.model.dto.NotificationDto;
import com.halo.core_bridge.api.schedule.notification.model.enums.NotificationType;
import com.halo.core_bridge.api.schedule.notification.service.NotificationService;
import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
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

        log.info("✅ 공고 생성 완료: id={}, title={}, assignedTo={}",
                saved.getId(), saved.getTitle(), assignee.getId());

        // ⭐⭐⭐ 개선: 담당자와 생성자 모두에게 알림 전송 ⭐⭐⭐
        try {
            // 1. 담당자에게 알림
            notificationService.createAndDispatch(
                    NotificationDto.Request.builder()
                            .userId(assignee.getId())
                            .role(UserRoleType.valueOf(assignee.getUserRole().getCode()))
                            .type(NotificationType.JOB_SCHEDULE_CREATED)
                            .title("새로운 채용 일정이 추가되었습니다.")
                            .message(dto.getTitle() + " 공고가 등록되었습니다.")
                            .build()
            );
            log.info("📤 담당자 알림 전송: userId={}", assignee.getId());

            // 2. 현재 로그인 유저(생성자)에게도 알림 (담당자와 다른 경우만)
            Long currentUserId = getCurrentUserId();
            if (currentUserId != null && !currentUserId.equals(assignee.getId())) {
                notificationService.createAndDispatch(
                        NotificationDto.Request.builder()
                                .userId(currentUserId)
                                .type(NotificationType.JOB_SCHEDULE_CREATED)
                                .title("채용 공고가 등록되었습니다.")
                                .message(dto.getTitle() + " 공고를 등록했습니다.")
                                .build()
                );
                log.info("📤 생성자 알림 전송: userId={}", currentUserId);
            }

        } catch (Exception ex) {
            log.error("❌ 알림 전송 실패: scheduleId={}", saved.getId(), ex);
            // 알림 실패해도 공고 생성은 성공으로 처리
        }

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

        log.info("✅ 공고 수정 완료: id={}, title={}", saved.getId(), saved.getTitle());

        // ⭐⭐⭐ 수정 알림 활성화 ⭐⭐⭐
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId != null) {
                notificationService.createAndDispatch(
                        NotificationDto.Request.builder()
                                .userId(currentUserId)
                                .type(NotificationType.JOB_SCHEDULE_UPDATED)
                                .title("채용 공고가 수정되었습니다.")
                                .message(dto.getTitle() + " 공고가 수정되었습니다.")
                                .build()
                );
                log.info("📤 수정 알림 전송: userId={}", currentUserId);
            }

            // 담당자가 변경된 경우, 새 담당자에게도 알림
            if (!assignee.getId().equals(currentUserId)) {
                notificationService.createAndDispatch(
                        NotificationDto.Request.builder()
                                .userId(assignee.getId())
                                .type(NotificationType.JOB_SCHEDULE_UPDATED)
                                .title("채용 공고가 수정되었습니다.")
                                .message(dto.getTitle() + " 공고가 수정되어 담당자로 지정되었습니다.")
                                .build()
                );
                log.info("📤 담당자 알림 전송: userId={}", assignee.getId());
            }
        } catch (Exception ex) {
            log.error("❌ 수정 알림 전송 실패: scheduleId={}", saved.getId(), ex);
        }

        return JobPostingScheduleDto.toDto(saved);
    }

    public void delete(Long id) {
        JobPostingSchedule e = repository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.SCHEDULE_SHARE_NOT_FOUND));

        String title = e.getTitle();
        repository.delete(e);

        log.info("✅ 공고 삭제 완료: id={}, title={}", id, title);

        // ⭐⭐⭐ 삭제 알림 활성화 ⭐⭐⭐
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId != null) {
                notificationService.createAndDispatch(
                        NotificationDto.Request.builder()
                                .userId(currentUserId)
                                .type(NotificationType.JOB_SCHEDULE_DELETED)
                                .title("채용 공고가 삭제되었습니다.")
                                .message(title + " 공고가 삭제되었습니다.")
                                .build()
                );
                log.info("📤 삭제 알림 전송: userId={}", currentUserId);
            }
        } catch (Exception ex) {
            log.error("❌ 삭제 알림 전송 실패: scheduleId={}", id, ex);
        }
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

        int sharedCount = 0;
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
            sharedCount++;

            // ⭐⭐⭐ 공유 알림 전송 ⭐⭐⭐
            try {
                notificationService.createAndDispatch(
                        NotificationDto.Request.builder()
                                .userId(uid)
                                .type(NotificationType.JOB_PROCESS_SHARED)
                                .title("채용 공고가 공유되었습니다.")
                                .message(schedule.getTitle() + " 공고가 공유되었습니다.")
                                .build()
                );
            } catch (Exception ex) {
                log.error("❌ 공유 알림 전송 실패: userId={}", uid, ex);
            }
        }

        log.info("✅ 공고 공유 완료: scheduleId={}, sharedCount={}", id, sharedCount);
    }

    @Transactional
    public void bulkShare(JobPostingScheduleDto.BulkShareRequest req) {
        int totalShared = 0;

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
                totalShared++;

                // ⭐⭐⭐ 일괄 공유 알림 전송 ⭐⭐⭐
                try {
                    notificationService.createAndDispatch(
                            NotificationDto.Request.builder()
                                    .userId(userId)
                                    .type(NotificationType.JOB_PROCESS_SHARED)
                                    .title("채용 공고가 공유되었습니다.")
                                    .message(schedule.getTitle() + " 외 " + (req.getJobs().size() - 1) + "개 공고가 공유되었습니다.")
                                    .build()
                    );
                } catch (Exception ex) {
                    log.error("❌ 일괄 공유 알림 전송 실패: userId={}", userId, ex);
                }
            }
        }

        log.info("✅ 일괄 공유 완료: totalShared={}", totalShared);
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

    /**
     * 현재 로그인한 사용자 ID 조회
     */
    private Long getCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof com.halo.core_bridge.api.users.model.dto.UserDto.Auth) {
                com.halo.core_bridge.api.users.model.dto.UserDto.Auth userAuth =
                        (com.halo.core_bridge.api.users.model.dto.UserDto.Auth) auth.getPrincipal();
                return userAuth.getId();
            }
        } catch (Exception e) {
            log.warn("현재 사용자 ID 조회 실패", e);
        }
        return null;
    }
}
