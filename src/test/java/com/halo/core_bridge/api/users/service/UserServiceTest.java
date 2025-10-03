package com.halo.core_bridge.api.users.service;

import com.halo.core_bridge.api.users.model.Gender;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입")
    void save() {

        // given
        User willReturnUser = User.builder()
                .id(1L)
                .email("test01@test.com")
                .birth(LocalDate.of(1996, 12, 16))
                .password("12345678")
                .phone("010-1234-5678")
                .gender(Gender.Male)
                .name("test")
                .userRole(
                        UserRole.builder().id(1).build()
                )
                .build();

        BDDMockito.given(userRepository.save(any(User.class))).willReturn(willReturnUser);

        UserDto.Create createUser = UserDto.Create.builder()
                .email("test01@test.com")
                .birth(LocalDate.of(1996, 12, 16))
                .password("12345678")
                .phone("010-1234-5678")
                .gender(Gender.Male)
                .name("test")
                .build();

        // when
        Long savedKey = userService.save(createUser);

        // then
        Assertions.assertThat(willReturnUser.getId()).isEqualTo(savedKey);
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 예외 발생")
    void saveExceptionDuplication() {

        // given
        // 내부 cause: Hibernate ConstraintViolationException
        SQLException sqlException = new SQLException("Duplicate entry", "23000", 1062);
        ConstraintViolationException hibernateEx =
                new ConstraintViolationException("중복 키", sqlException, "UK_USER_EMAIL");

        // Spring Data JPA가 던지는 예외 흉내
        DataIntegrityViolationException springEx =
                new DataIntegrityViolationException("무결성 위반", hibernateEx);

        BDDMockito.given(userRepository.save(any(User.class))).willThrow(springEx);

        UserDto.Create createUser = UserDto.Create.builder()
                .email("test01@test.com")
                .birth(LocalDate.of(1996, 12, 16))
                .password("12345678")
                .phone("010-1234-5678")
                .gender(Gender.Male)
                .name("test")
                .build();

        // when
        // then
        assertThrows(BaseException.class, () -> userService.save(createUser));
    }
}