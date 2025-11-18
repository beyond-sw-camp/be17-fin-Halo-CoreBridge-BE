package com.halo.core_bridge.api.mail.service;

import com.halo.core_bridge.api.mail.model.MailSend;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class PasswordResetMailService extends BaseMailService {

    private final StringRedisTemplate redisTemplate;

    @Value("${password.reset.redirect.url}")
    private String REDIRECT_LINK;
    protected String uuid;
    protected String email;

    public PasswordResetMailService(JavaMailSender mailSender, StringRedisTemplate redisTemplate) {
        super(mailSender, MailSend.PASSWORD_RESET_MAIL.getSubject());
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sendToEmail(String email) {

        this.email = email;
        uuid = createUuid();
        MimeMessage mimeMessage = createMimeMessage(email);

        if (getTimeout() == null) {
            redisTemplate.opsForValue().set(createRedisKey(email), uuid);
        } else {
            redisTemplate.opsForValue().set(createRedisKey(email), uuid, getTimeout());
        }

        mailSender.send(mimeMessage);
    }

    protected Duration getTimeout() {
        return Duration.ofMinutes(5);
    }

    @Override
    protected String createView() {
        String rawView = """
                 <html>
                     <head>
                     </head>
                 <body>
                     <p>
                         안녕하세요.<br>
                         저희 <b>CoreBridge</b>을 이용해 주셔서 감사합니다.<br>
                         비밀번호 재설정 링크를 보내드립니다.
                         아래의 링크에 접속하여 비밀번호를 재설정해주세요.
                     </p>
                 <strong style="font-size:20px">
                     <a href="%s"> 비밀번호 재설정 바로가기 </a>
                 </strong>
                     <p>
                         <font size=3><b>개인정보 보호를 위해서 링크는 5분간 유지됩니다.</b></font>
                     </p>
                 </body>
                 </html>
                """;
        return String.format(rawView, createURI(uuid, email));
    }

    private String createUuid() {
        return UUID.randomUUID().toString();
    }

    protected String createURI(String uuid, String email) {
        return REDIRECT_LINK.concat("?token=").concat(uuid).concat("&email=").concat(email);
    }

    private String createRedisKey(String email) {
        return MailSend.PASSWORD_RESET_MAIL.createRedisKey(email);
    }
}
