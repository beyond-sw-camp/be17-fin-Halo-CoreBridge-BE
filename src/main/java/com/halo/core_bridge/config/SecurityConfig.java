package com.halo.core_bridge.config;

import com.halo.core_bridge.api.token.filter.AlreadyLoginFilter;
import com.halo.core_bridge.api.token.filter.JwtAuthFilter;
import com.halo.core_bridge.api.token.filter.LoginFilter;
import com.halo.core_bridge.api.token.jwt.JwtTokenService;
import com.halo.core_bridge.api.token.refresh.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (auth) -> auth
//                        .requestMatchers("/login", "/logout", "/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
//                        .anyRequest().permitAll()
                        .requestMatchers("/**").permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        addFilter(http);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void addFilter(HttpSecurity http) throws Exception {

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(refreshTokenService, jwtTokenService);
        AlreadyLoginFilter alreadyLoginFilter = new AlreadyLoginFilter(refreshTokenService, jwtTokenService);
        LoginFilter loginFilter = new LoginFilter(refreshTokenService, jwtTokenService, authenticationConfiguration.getAuthenticationManager());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(alreadyLoginFilter, JwtAuthFilter.class);
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
