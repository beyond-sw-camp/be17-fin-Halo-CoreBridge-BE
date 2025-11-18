package com.halo.core_bridge.api.users.model.dto;

import com.halo.core_bridge.api.users.model.Gender;
import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class UserDto {

    @Builder
    @Getter
    public static class Create {

        @NotBlank(message = "이메일을 입력하세요.")
        @Email
        private String email;

        @NotBlank(message = "비밀번호를 입력하세요.")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야합니다.")
        private String password;

        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @NotBlank(message = "연락처를 입력하세요.")
        @Pattern(
                regexp = "^010-\\d{4}-\\d{4}$",
                message = "올바른 전화번호 형식으로 입력해주세요."
        )
        private String phone;

        @NotNull(message = "생년월일을 입력하세요.")
        @DateTimeFormat(pattern = "yyyy-mm-dd")
        private LocalDate birth;

        @NotNull(message = "성별을 선택하세요.")
        private Gender gender;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .phone(phone)
                    .birth(birth)
                    .gender(gender)
                    .userRole(
                            UserRole.builder().id(2).build()
                    )
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth implements UserDetails {

        private Long id;
        private String email;
        private String password;
        private String name;
        private String role;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role));
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return email;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        private String email;
        private String password;
    }

    @Getter
    @Builder
    public static class ResumeUserInfo  {

        private String name;
        private String email;
        private String gender;
        private String phone;
        private LocalDate birth;

        public static ResumeUserInfo from(User entity) {
            return ResumeUserInfo.builder()
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .gender(entity.getGender().getName())
                    .birth(entity.getBirth())
                    .phone(entity.getPhone())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Read {

        private Long id;
        private String name;
        private String email;
        private LocalDate birth;
        private String gender;
        private String phone;
        private String type;
        private String profileUrl;

        public static Read from(User entity) {
            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .birth(entity.getBirth())
                    .gender(entity.getGender().getName())
                    .phone(entity.getPhone())
                    .type(entity.getUserRole().getName())
                    .profileUrl(entity.getProfileImage().get(0).getSavedPath())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class LoginResponse {

        private String name;
        private String role;
        private String email;

        public static LoginResponse from(Auth authUser) {
            return LoginResponse.builder()
                    .name(authUser.getName())
                    .role(UserRoleType.getDisplayName(authUser.getRole()))
                    .email(authUser.getEmail())
                    .build();
        }
    }
}
