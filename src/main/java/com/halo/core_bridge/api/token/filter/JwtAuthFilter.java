package com.halo.core_bridge.api.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.token.jwt.JwtTokenService;
import com.halo.core_bridge.api.token.refresh.model.dto.RefreshTokenDto;
import com.halo.core_bridge.api.token.refresh.service.RefreshTokenService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import com.halo.core_bridge.utils.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            accessToken = CookieUtil.getCookieValue(request, jwtTokenService.getTokenName());
            refreshToken = CookieUtil.getCookieValue(request, refreshTokenService.getTokenName());
        }

        if (accessToken != null) {

            try {

                UserDto.Auth userAuth = jwtTokenService.toUserAuth(accessToken);
                // Authentication 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userAuth,
                        null,
                        List.of(new SimpleGrantedAuthority(userAuth.getRole()))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                // 유효기간 만료시 재발급
                log.error("AccessToken 만료, RefreshToken 사용하여 AccessToken 재발급 시도");

                if (refreshToken != null) {

                    // RefreshToken 검증
                    RefreshTokenDto.Meta refreshTokenMeta = refreshTokenService.validateAndGetMeta(refreshToken);

                    // AccessToken 재발급
                    String reIssuedAccessToken = jwtTokenService.generateToken(refreshTokenMeta.getUserId(), refreshTokenMeta.getRole());

                    // AccessToken 재발급이 성공하면 RefreshToken Rotation
                    if (reIssuedAccessToken != null) {

                        // RefreshToken Rotation
                        String reIssuedRefreshToken = refreshTokenService.rotation(refreshToken);

                        // AccessToken, RefreshToken Cookie 생성

                        CookieUtil.addAccessTokenCookie(response, reIssuedAccessToken, jwtTokenService.getTokenName(), jwtTokenService.getExpiration());
                        CookieUtil.addRefreshTokenCookie(response, reIssuedRefreshToken, refreshTokenService.getTokenName(), refreshTokenService.getExpiration());

                        handleSuccessReIssueToken(response);
                        return;
                    }
                }

                log.error("재발급 실패, 로그아웃 처리 됩니다.");
                handleTokenException(response, BaseResponseStatus.EXCEPTION_CREATE_ACCESS_TOKEN);
                return;

            } catch (Exception e) {

                log.error("토큰 예외 발생, 로그아웃 처리 됩니다.");
                log.error(e.getMessage());
                handleTokenException(response, BaseResponseStatus.INVALID_JWT);
                return;

            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleSuccessReIssueToken(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        BaseResponse.success("토큰 재발급 성공")
                )
        );
    }

    private void handleTokenException(HttpServletResponse response, BaseResponseStatus status) throws IOException {

        // 쿠키 삭제
        CookieUtil.deleteCookie(response, jwtTokenService.getTokenName());
        CookieUtil.deleteCookie(response, refreshTokenService.getTokenName());
        SecurityContextHolder.clearContext();

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        BaseResponse.error(status)
                )
        );
    }
}
