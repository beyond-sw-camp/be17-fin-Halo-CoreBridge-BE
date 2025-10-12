package com.halo.core_bridge.api.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.token.jwt.JwtTokenService;
import com.halo.core_bridge.api.token.refresh.service.RefreshTokenService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import com.halo.core_bridge.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AlreadyLoginFilter extends OncePerRequestFilter {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            if (request.getRequestURI().equals("/login") && authentication != null) {

                String refreshToken = CookieUtil.getCookieValue(request, refreshTokenService.getTokenName());
                refreshTokenService.delete(refreshToken);
            }
        } catch (BaseException e) {
            handleException(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response) throws IOException {
        CookieUtil.deleteCookie(response, jwtTokenService.getTokenName());
        CookieUtil.deleteCookie(response, refreshTokenService.getTokenName());
        SecurityContextHolder.clearContext();

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        BaseResponse.error(BaseResponseStatus.INVALID_REFRESH_TOKEN)
                )
        );
    }
}
