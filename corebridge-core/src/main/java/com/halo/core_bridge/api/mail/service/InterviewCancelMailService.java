package com.halo.core_bridge.api.mail.service;

import com.halo.core_bridge.api.mail.model.MailSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InterviewCancelMailService extends BaseMailService {


    protected InterviewCancelMailService(JavaMailSender mailSender) {
        super(mailSender, MailSend.INTERVIEW_CANCEL_MAIL.getSubject());
    }

    @Override
    @Async
    public void sendToEmail(String email, String cancelReason) {
        try {
            String html = """
                <html>
                  <body>
                    <h3>면접 취소 안내</h3>
                    <p>안녕하세요. 다음과 같은 사유로 면접일정이 취소되었습니다.</p>
                    <ul>
                       %s
                    </ul>
                  </body>
                </html>
                """.formatted(cancelReason);

            super.sendToEmail(email, html);
        } catch (Exception e) {
            log.error("면접 취소 메일 발송 실패", e);
        }

    }

    @Override
    protected String createView() {
        return "";
    }
}
