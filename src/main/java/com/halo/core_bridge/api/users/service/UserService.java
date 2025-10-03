package com.halo.core_bridge.api.users.service;

import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long save(UserDto.Create createUser) {
        try {

            User savedUser = userRepository.save(createUser.toEntity());
            return savedUser.getId();

        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException cve) {
                if (cve.getConstraintName().contains("EMAIL")) {
                    throw BaseException.from(BaseResponseStatus.DUPLICATE_USER_EMAIL);
                }
            }

            throw e;
        }
    }
}
