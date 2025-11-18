package com.halo.core_bridge.api.token.jwt;

import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final SecretKeyManager secretKeyManager;

    @Getter
    @Value("${app.token.access.name}")
    private String tokenName;

    @Getter
    @Value("${app.token.access.expiration}")
    private Long expiration;

    private final String id = "id";
    private final String role = "role";

    public String generateToken(Long id, String role) {

        Map<String, String> claims =  new HashMap<>();
        claims.put(this.id, String.valueOf(id));
        claims.put(this.role, role);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String getValue(Claims claims, String key) {
        return (String) claims.get(key);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UserDto.Auth toUserAuth(String token) {
        Claims claims = getClaims(token);

        String id = getValue(claims, this.id);
        String role = getValue(claims, this.role);

        if (id == null || role == null) {
            throw BaseException.from(BaseResponseStatus.INVALID_JWT);
        }

        return UserDto.Auth.builder()
                .id(Long.parseLong(id))
                .role(role)
                .build();
    }

    private SecretKey getSecretKey() {
        return secretKeyManager.getSecretKey();
    }
}
