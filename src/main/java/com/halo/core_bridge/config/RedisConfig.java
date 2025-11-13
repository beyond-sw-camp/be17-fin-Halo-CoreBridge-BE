package com.halo.core_bridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.halo.core_bridge.api.schedule.notification.infra.RedisNotificationSubscriber;
import com.halo.core_bridge.api.token.refresh.model.dto.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisNotificationSubscriber redisNotificationSubscriber;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, RefreshTokenDto.Meta> refreshTokenRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RefreshTokenDto.Meta> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    // ✅ Notification 용 RedisTemplate (JSON 직렬화)
    @Bean
    public RedisTemplate<String, Object> notificationRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // ✅ JavaTimeModule 등록된 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ✅ 권장 직렬화기: GenericJackson2JsonRedisSerializer
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    // ✅ 알림 채널
    @Bean
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("notification_channel");
    }

    // ✅ MessageListenerAdapter - Subscriber의 onMessage 메서드 연결
    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(redisNotificationSubscriber, "onMessage");
    }

    // ✅ 구독 컨테이너 (Subscriber를 채널에 등록)
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerAdapter,
            ChannelTopic notificationTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // ✅ 핵심: MessageListenerAdapter를 notificationTopic에 등록
        container.addMessageListener(messageListenerAdapter, notificationTopic);

        return container;
    }
}
