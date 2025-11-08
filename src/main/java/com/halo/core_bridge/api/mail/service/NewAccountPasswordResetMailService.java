package com.halo.core_bridge.api.mail.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NewAccountPasswordResetMailService extends PasswordResetMailService {

    public NewAccountPasswordResetMailService(JavaMailSender mailSender, StringRedisTemplate redisTemplate) {
        super(mailSender, redisTemplate);
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
                         계정이 발급되었습니다. 해당 계정은 사용전 비밀번호 재설정이 필요합니다.
                         아래의 링크에 접속하여 비밀번호를 재설정해주세요.
                     </p>
                 <strong style="font-size:20px">
                     <a href="%s"> 비밀번호 재설정 바로가기 </a>
                 </strong>
                 </body>
                 </html>
                """;
        return String.format(rawView, createURI(uuid, email));
    }

    @Override
    protected Duration getTimeout() {
        return null;
    }
}
