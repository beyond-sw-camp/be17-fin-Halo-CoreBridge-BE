package com.halo.core_bridge.api.mail.service;

import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import com.halo.core_bridge.api.mail.model.MailSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.halo.core_bridge.api.interview.model.dto.InterviewDto.Reminder;

@Slf4j
@Service
public class InterviewReminderMailService extends BaseMailService {

    private final InterviewRepository interviewRepository;

    public InterviewReminderMailService(JavaMailSender mailSender, InterviewRepository interviewRepository) {
        super(mailSender, MailSend.INTERVIEW_REMINDER_MAIL.getSubject());
        this.interviewRepository = interviewRepository;
    }

    @Async
    @Transactional
    public void sendToEmail(String email, Reminder reminder) {

        String html = """
                <html>
                  <body>
                    <h3>면접 일정 안내</h3>
                    <p>1시간 후 면접이 시작될 예정입니다.</p>
                    <ul>
                       <li><b>시작 시간:</b> %s</li>
                       <li><b>장소:</b> %s</li>
                    </ul>
                  </body>
                </html>
                """.formatted(
                reminder.getStartDate().toString() + " " + reminder.getStartTime().toString(),
                reminder.getLocation().equals("온라인") ? "온라인" : reminder.getLocation()
        );

        try {
            super.sendToEmail(email, html);

            // 메일 전송 성공 → DB 업데이트
            interviewRepository.markReminderSent(reminder.getId(), LocalDateTime.now());

            log.info("[InterviewReminderMailService] reminder success for interviewId={}", reminder.getId());

        } catch (Exception e) {
            log.error("Failed to send reminder (async) for interviewId={}", reminder.getId(), e);
        }
    }

    @Override
    protected String createView() {
        return ""; // 인터뷰 메일은 여기 안 씀
    }
}
