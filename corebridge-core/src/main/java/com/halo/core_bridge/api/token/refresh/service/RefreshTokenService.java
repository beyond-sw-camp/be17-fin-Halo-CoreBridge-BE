package com.halo.core_bridge.api.token.refresh.service;

import com.halo.core_bridge.api.token.refresh.model.dto.RefreshTokenDto;
import com.halo.core_bridge.api.token.refresh.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Getter
    @Value("${app.token.refresh.name}")
    private String tokenName;

    /**
     * RefreshToken을 생성한다.
     * @param meta Repository에 저장될 RefreshToken의 MetaData
     * @return 새로 생성된 RefreshToken
     */
    public String generateRefreshToken(RefreshTokenDto.Meta meta) {
        String refreshToken = UUID.randomUUID().toString();

        refreshTokenRepository.save(refreshToken, meta);
        return refreshToken;
    }

    /**
     * RefreshToken을 검증하고 MeataData를 반환한다.
     * @param refreshToken 검증할 RefreshToken
     * @return <code>RefreshTokenDto.Meta</code>
     */
    public RefreshTokenDto.Meta validateAndGetMeta(String refreshToken) {
        return refreshTokenRepository.findByKey(refreshToken);
    }

    /**
     * 기존의 RefreshToken을 삭제하고 재발급하는 로테이션을 수행한다.
     * @param oldRefreshToken 삭제할 RefreshToken
     * @return 새로 생성된 RefreshToken
     */
    public String rotation(String oldRefreshToken) {

        // 기존 리프레시토큰의 값을 가지고온다.
        RefreshTokenDto.Meta findMeta = refreshTokenRepository.findByKey(oldRefreshToken);

        // 기존 리프레시토큰을 삭제한다.
        refreshTokenRepository.deleteByKey(oldRefreshToken);

        // 새로운 리프레시토큰을 생성한다.
        return generateRefreshToken(findMeta);
    }

    /**
     * RefreshToken을 삭제한다.
     * @param refreshToken 삭제할 RefreshToken
     */
    public void delete(String refreshToken) {
        refreshTokenRepository.deleteByKey(refreshToken);
    }

    /**
     * RefreshToken의 Expiration을 조회한다.
     * @return <code>Long</code>
     */
    public Long getExpiration() {
        return refreshTokenRepository.getExpiration();
    }
}
