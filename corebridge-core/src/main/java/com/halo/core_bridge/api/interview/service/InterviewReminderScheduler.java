package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import com.halo.core_bridge.api.mail.service.InterviewReminderMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.Reminder;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewReminderScheduler {

    private static final ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");
    private static final InterviewStatus STATUS_SCHEDULED = InterviewStatus.SCHEDULED;
    private final InterviewRepository interviewRepository;
    private final InterviewReminderMailService interviewReminderMailService;

    @Scheduled(cron = "0 * * * * *")
    public void sendInterviewReminders() {
        LocalDateTime now = LocalDateTime.now(ZONE_KST);
        LocalDateTime from = now.plusHours(1);
        LocalDateTime to = from.plusMinutes(1);

        List<Interview> interviews = interviewRepository.findInterviewsToRemind(from, to, STATUS_SCHEDULED);

        if (interviews.isEmpty()) {
            return;
        }

        log.info("[InterviewReminderScheduler] {} interviews to remind ({} ~ {})",
                interviews.size(), from, to);

        for (Interview interview : interviews) {
            Reminder reminder = Reminder.from(interview);
            interviewReminderMailService.sendToEmail(reminder.getEmail(), reminder);
        }
    }
}
