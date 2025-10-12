package com.halo.core_bridge.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

public class CookieUtil {

    @Value("${app.cookie.secure}")
    private static boolean cookieSecure;

    public static void createCookie(HttpServletResponse response,
                              String cookieName,
                              String cookieValue,
                              boolean httpOnly,
                              String path,
                              int expire) {

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath(path);
        cookie.setMaxAge(expire);
        cookie.setSecure(cookieSecure);

        response.addCookie(cookie);
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, String cookieName, Long expire) {
        addAccessTokenCookie(response, refreshToken, cookieName,expire);
    }

    public static void addAccessTokenCookie(HttpServletResponse response, String accessToken, String cookieName, Long expire) {
        int exp = convertLongToIntSecond(expire);
        createCookie(response, cookieName, accessToken, cookieSecure, "/", exp);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    public static void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static int convertLongToIntSecond(Long value) {
        return Math.toIntExact(value) / 1000;
    }
}
