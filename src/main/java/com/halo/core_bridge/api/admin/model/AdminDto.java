package com.halo.core_bridge.api.admin.model;

import com.halo.core_bridge.api.users.model.UserRoleType;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AdminDto {

    @Getter
    public static class UserCreate {

        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @Email
        @NotBlank(message = "이메일을 입력하세요.")
        private String email;

        @NotBlank(message = "권한을 선택하세요.")
        private String roleType;

        public User toEntity(UserRole userRole) {
            return User.builder()
                    .name(name)
                    .email(email)
                    .userRole(userRole)
                    .build();
        }
    }
}
