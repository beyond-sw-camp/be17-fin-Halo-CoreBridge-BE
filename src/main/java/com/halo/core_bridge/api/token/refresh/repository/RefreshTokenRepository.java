package com.halo.core_bridge.api.token.refresh.repository;

import com.halo.core_bridge.api.token.refresh.model.dto.RefreshTokenDto;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final String prefix = "RT:";

    @Getter
    @Value("${app.token.refresh.expiration}")
    private Long expiration;

    private final RedisTemplate<String, RefreshTokenDto.Meta> redisTemplate;

    /**
     * RefreshToken을 저장한다.
     * @param token 저장할 RefreshToken
     * @param refreshTokenMeta RefreshToken과 함께 value로 저장될 MetaData
     */
    public void save(String token, RefreshTokenDto.Meta refreshTokenMeta) {
        redisTemplate.opsForValue().set(prefix + token, refreshTokenMeta, Duration.ofMillis(expiration));
    }

    /**
     * RefreshToken을 사용해 <code>value</code>값을 찾는다.
     * @param refreshToken key로 사용할 RefreshToken
     * @return <code>RefreshTokenDto.Meta</code> value 값으로 저장된 MetaData
     * @throws BaseException RefreshToken으로 조회한 value가 null인 경우 <code>BaseResponseStatus.INVALID_REFRESH_TOKEN</code> 예외 발생
     */
    public RefreshTokenDto.Meta findByKey(String refreshToken) {
        RefreshTokenDto.Meta findMeta = redisTemplate.opsForValue().get(prefix + refreshToken);

        if (findMeta == null) {
            throw BaseException.from(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        return findMeta;
    }

    /**
     * RefreshToken을 삭제한다.
     * @param refreshToken 삭제할 RefreshToken
     * @throws BaseException 삭제 결과가 false라면 <code>BaseResponseStatus.INVALID_REFRESH_TOKEN</code> 예외 발생
     */
    public void deleteByKey(String refreshToken) {
        Boolean deleteResult = redisTemplate.delete(prefix + refreshToken);

        if (Boolean.FALSE.equals(deleteResult)) {
            throw BaseException.from(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }
    }
}
