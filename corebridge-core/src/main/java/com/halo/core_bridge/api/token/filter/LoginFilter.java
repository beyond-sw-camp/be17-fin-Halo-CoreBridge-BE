package com.halo.core_bridge.api.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.token.jwt.JwtTokenService;
import com.halo.core_bridge.api.token.refresh.model.dto.RefreshTokenDto;
import com.halo.core_bridge.api.token.refresh.service.RefreshTokenService;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import com.halo.core_bridge.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken;
        try {

            UserDto.Login dto = new ObjectMapper().readValue(request.getInputStream(), UserDto.Login.class);
            authToken = new UsernamePasswordAuthenticationToken(
                    dto.getEmail(), dto.getPassword(), null
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        UserDto.Auth authUser = (UserDto.Auth) authResult.getPrincipal();

        String accessToken = jwtTokenService.generateToken(
                authUser.getId(),
                authUser.getRole()
        );

        if(accessToken != null) {
            log.info("AccessToken 발급 성공");

            createRefreshTokenCookie(response, authUser.getId(), authUser.getRole());
            CookieUtil.addAccessTokenCookie(response, accessToken, jwtTokenService.getTokenName(), jwtTokenService.getExpiration());

            response.setContentType("application/json; charset=UTF-8");

            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            BaseResponse.success(UserDto.LoginResponse.from(authUser))
                    )
            );

            return;
        }

        throw BaseException.from(BaseResponseStatus.EXCEPTION_CREATE_ACCESS_TOKEN);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        log.error(failed.getMessage());

        CookieUtil.deleteCookie(response, refreshTokenService.getTokenName());
        CookieUtil.deleteCookie(response, jwtTokenService.getTokenName());
        SecurityContextHolder.clearContext();

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        BaseResponse.error(BaseResponseStatus.INVALID_USER_INFO)
                )
        );
    }

    private void createRefreshTokenCookie(HttpServletResponse response, Long userId, String role) {
        log.info("RefreshToken 발급 진행");

        String generatedToken = refreshTokenService.generateRefreshToken(RefreshTokenDto.Meta.from(userId, role));
        CookieUtil.addRefreshTokenCookie(response, generatedToken, refreshTokenService.getTokenName(), refreshTokenService.getExpiration());
    }
}
