package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewAutoStartScheduler {

    private final InterviewRepository interviewRepository;

    /**
     * 매 1분마다 예약시간이 지난 INTERVIEW 자동 시작
     */
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void autoStartInterviews() {

        LocalDateTime now = LocalDateTime.now();

        List<Interview> scheduled = interviewRepository.findByStatusAndStartDateTimeBefore(InterviewStatus.SCHEDULED, now);

        for (Interview interview : scheduled) {
            interview.startInterview();
        }
    }
}
