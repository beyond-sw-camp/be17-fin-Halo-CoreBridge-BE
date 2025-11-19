package com.halo.core_bridge.config;

import com.halo.core_bridge.api.token.filter.AlreadyLoginFilter;
import com.halo.core_bridge.api.token.filter.JwtAuthFilter;
import com.halo.core_bridge.api.token.filter.LoginFilter;
import com.halo.core_bridge.api.token.jwt.JwtTokenService;
import com.halo.core_bridge.api.token.refresh.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://www.core-bridge.co.kr"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 적용
        return source;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (auth) -> auth
                        /* ===========================
                         * permitAll()
                         * =========================== */
                        .requestMatchers(
                                HttpMethod.POST, "/api/users"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/info",
                                "/api/users/resume-info",
                                "/api/job-postings/header/**",
                                "/api/job-postings/{id}",
                                "/api/jobs/search",
                                "/api/tech-stacks"
                        ).permitAll()


                        /* ===========================
                         * APPLICANT ONLY
                         * =========================== */
                        .requestMatchers(
                                HttpMethod.POST, "/api/pdf", "/api/image",
                                "/api/jobposts/*/applies",
                                "/api/jobposts/*/applies/*/cover-letter-descriptions"
                        ).hasRole("APPLICANT")

                        .requestMatchers(
                                HttpMethod.PATCH, "/api/jobposts/*/applies/*"
                        ).hasRole("APPLICANT")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/pdf/{idx}",
                                "/api/image/{idx}",
                                "/api/jobposts/*/applies/*"
                        ).hasRole("APPLICANT")


                        /* ===========================
                         * AUTHENTICATED (모든 로그인 사용자)
                         * =========================== */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pdf/download/**",
                                "/api/pdf/find/**",
                                "/api/pdf/view/**",
                                "/api/image/{idx}",
                                "/api/jobposts/*/applies/*",
                                "/api/jobposts/*/applies/*/cover-letter-descriptions",
                                "/api/jobposts/*/applies/cover-letter-titles"
                        ).authenticated()


                        /* ===========================
                         * INTERVIEWER ONLY
                         * =========================== */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interview/chat/*/applicant"
                        ).hasRole("INTERVIEWER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/interviewer/evaluation"
                        ).hasRole("INTERVIEWER")


                        /* ===========================
                         * INTERVIEWER + RECRUITER + ADMIN
                         * =========================== */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/recruiter/interviews",
                                "/api/recruiter/interviews/*"
                        ).hasAnyRole("INTERVIEWER", "RECRUITER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviewer/evaluation-criteria"
                        ).hasAnyRole("INTERVIEWER", "RECRUITER", "ADMIN")


                        /* ===========================
                         * RECRUITER + ADMIN
                         * =========================== */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/jobposts/*/applies",
                                "/api/job-postings",
                                "/api/job-postings/search",
                                "/api/job-postings/*/edit",
                                "/api/recruiter/processes",
                                "/api/jobs/*/management",
                                "/api/department",
                                "/api/schedules/**",
                                "/api/recruiter/jobs/**",
                                "/api/interviewers",
                                "/api/interviewers/jobPosting"
                        ).hasAnyRole("RECRUITER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/recruiter/interviews",
                                "/api/recruiter/interviews/*/cancel",
                                "/api/recruiter/processes",
                                "/api/jobposts/*/applies/search",
                                "/api/schedules/**",
                                "/api/recruiter/jobs/**"
                        ).hasAnyRole("RECRUITER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/job-postings/*",
                                "/api/recruiter/processes",
                                "/api/recruiter/processes/*",
                                "/api/jobs/*/management/*/process/*"
                        ).hasAnyRole("RECRUITER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/schedules/**",
                                "/api/recruiter/jobs/**"
                        ).hasAnyRole("RECRUITER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/job-postings/*",
                                "/api/recruiter/processes/*",
                                "/api/schedules/**",
                                "/api/recruiter/jobs/**"
                        ).hasAnyRole("RECRUITER", "ADMIN")


                        .anyRequest().permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

        http.cors(cors ->
                cors.configurationSource(corsConfigurationSource()));

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
        loginFilter.setFilterProcessesUrl("/api/login");

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(alreadyLoginFilter, JwtAuthFilter.class);
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
