package com.halo.core_bridge.api.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public abstract class BaseMailService {

    protected final JavaMailSender mailSender;

    protected static final boolean IS_HTML = true;
    protected static final String DEFAULT_ENCODE = "UTF-8";

    protected String subject = "[CoreBridge] 안녕하세요. CoreBridge에서 알립니다.";

    protected BaseMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public BaseMailService(JavaMailSender mailSender, String subject) {
        this.mailSender = mailSender;
        this.subject = subject;
    }

    public void sendToEmail(String email) {
        MimeMessage mimeMessage = createMimeMessage(email);
        mailSender.send(mimeMessage);
    }

    /**
     * MimeMessage 생성
     *
     * @param email 전송할 이메일
     * @return MimeMessage
     */
    protected MimeMessage createMimeMessage(String email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, DEFAULT_ENCODE);
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(createView(), IS_HTML);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }

        return mimeMessage;
    }

    /**
     * 이메일에 보여주기 위한 내용 추가
     * @return String 형식의 html
     */
    protected abstract String createView();
}
