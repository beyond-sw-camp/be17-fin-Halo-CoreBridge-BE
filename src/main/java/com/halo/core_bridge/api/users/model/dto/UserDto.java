package com.halo.core_bridge.api.users.model.dto;

import com.halo.core_bridge.api.users.model.Gender;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
}
