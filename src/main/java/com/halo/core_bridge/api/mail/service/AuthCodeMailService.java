package com.halo.core_bridge.api.mail.service;

import com.halo.core_bridge.api.auth.service.AuthCodeCreator;
import com.halo.core_bridge.api.mail.model.MailSend;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class AuthCodeMailService extends BaseMailService {


    private final AuthCodeCreator authCodeCreator;
    private final StringRedisTemplate redisTemplate;

    private String authCode;

    public AuthCodeMailService(JavaMailSender mailSender, AuthCodeCreator authCodeCreator, StringRedisTemplate redisTemplate) {
        super(mailSender, MailSend.AUTH_CODE_MAIL.getSubject());
        this.authCodeCreator = authCodeCreator;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sendToEmail(String email) {
        authCode = authCodeCreator.generateAuthCode();

        MimeMessage mimeMessage = createMimeMessage(email);
        redisTemplate.opsForValue().set(createRedisKey(email), authCode, Duration.ofMinutes(5));

        mailSender.send(mimeMessage);
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
                         요청하신 인증 번호를 보내드립니다.
                     </p>
                 <strong style="font-size:40px">
                     %s
                 </strong>
                     <p>
                         위의 인증번호 6자리를 인증번호 입력창에 입력해주세요.<br>
                         <font size=1><b>개인정보 보호를 위해서 인증번호는 5분간 유지됩니다.</b></font>
                     </p>
                 </body>
                 </html>
                """;
        return String.format(rawView, authCode);
    }

    private String createRedisKey(String email) {
        return MailSend.AUTH_CODE_MAIL.createRedisKey(email);
    }
}
