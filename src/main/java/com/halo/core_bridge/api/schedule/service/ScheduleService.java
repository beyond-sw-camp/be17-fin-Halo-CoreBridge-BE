package com.halo.core_bridge.api.schedule.service;

import com.halo.core_bridge.api.schedule.model.dto.ScheduleDto;
import com.halo.core_bridge.api.schedule.model.entity.Schedule;
import com.halo.core_bridge.api.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    /** 일정 등록 */
    @Transactional
    public ScheduleDto.RegistrationResult register(ScheduleDto.Registration dto) {
        Schedule schedule = scheduleRepository.save(dto.toEntity());
        schedule.assignOriginalNo(schedule.getScheduleId()); // 원본 일정 번호(scheduleOriNo) 를 지정
        return ScheduleDto.RegistrationResult.of(schedule.getScheduleId());
    }

    /** 일정 목록 조회 */
    @Transactional(readOnly = true)
    public List<ScheduleDto.Response> getSchedules(String ownerId, Integer year, Integer month) {
        return scheduleRepository.findSchedulesByYearAndMonth(ownerId, year, month)
                .stream()
                .map(ScheduleDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    /** 단일 일정 조회 */
    @Transactional(readOnly = true)
    public ScheduleDto.Response getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));
        return ScheduleDto.Response.fromEntity(schedule);
    }

    /** 일정 수정 + 복제본 동기화 */
    @Transactional
    public void updateSchedule(Long id, ScheduleDto.Update dto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        schedule.updatePartial(dto);

        List<Schedule> clones = scheduleRepository.findClonesByOriginId(schedule.getScheduleOriNo(), id);
        clones.forEach(clone -> clone.updateByOrigin(schedule));

        log.info("✅ 원본({}) 및 복제본 {}개 동기화 완료", id, clones.size());
    }

    /** 일정 삭제 */
    @Transactional
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 일정이 존재하지 않습니다.");
        }
        scheduleRepository.deleteById(id);
    }

    /** 여러 사용자에게 일정 공유 */
    @Transactional
    public List<ScheduleDto.CloneResult> shareScheduleToMultiple(Long scheduleId, String ownerId, List<String> targetUserIds) {
        Schedule origin = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("공유할 일정이 존재하지 않습니다."));

        if (!origin.getScheduleOwnerId().equals(ownerId)) {
            throw new IllegalStateException("자신의 일정만 공유할 수 있습니다.");
        }

        return targetUserIds.stream().map(targetUserId -> {
            Schedule clone = Schedule.builder()
                    .scheduleOriNo(origin.getScheduleOriNo() != null ? origin.getScheduleOriNo() : origin.getScheduleId())
                    .scheduleOwnerId(targetUserId)
                    .scheduleOriOwnerId(origin.getScheduleOwnerId())
                    .scheduleYear(origin.getScheduleYear())
                    .scheduleMonth(origin.getScheduleMonth())
                    .scheduleDate(origin.getScheduleDate())
                    .scheduleTitle(origin.getScheduleTitle())
                    .scheduleBody(origin.getScheduleBody())
                    .build();

            Schedule saved = scheduleRepository.save(clone);
            log.info("✅ 일정 {}이 사용자 {}에게 공유됨", scheduleId, targetUserId);
            return ScheduleDto.CloneResult.success(saved);
        }).collect(Collectors.toList());
    }
}
