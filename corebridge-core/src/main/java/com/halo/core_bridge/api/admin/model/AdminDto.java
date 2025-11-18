package com.halo.core_bridge.api.admin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Builder
    public static class Account {

        private Long id;
        private String name;
        private String email;
        private String roleType;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        public static Account from(User entity) {

            return Account.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .createdAt(entity.getCreatedAt())
                    .roleType(entity.getUserRole().getName())
                    .build();

        }
    }

    @Getter
    @Builder
    public static class AccountList {

        private List<Account> accounts;
        private int currentPage;
        private int totalPages;
        private long totalElements;

        public static AccountList from(Page<User> users) {
            return AccountList.builder()
                    .accounts(
                            users.getContent().stream().map(Account::from).toList()
                    )
                    .currentPage(users.getNumber())
                    .totalElements(users.getTotalElements())
                    .totalPages(users.getTotalPages())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class AccountInfiniteList {

        private List<Account> accounts;
        private int currentPage;
        private boolean hasNext;


        public static AccountInfiniteList from(Slice<User> users) {

            return AccountInfiniteList.builder()
                    .accounts(
                            users.getContent().stream().map(Account::from).toList()
                    )
                    .currentPage(users.getNumber())
                    .hasNext(users.hasNext())
                    .build();
        }
    }
}
