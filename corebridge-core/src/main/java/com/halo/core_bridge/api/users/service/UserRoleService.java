package com.halo.core_bridge.api.users.service;

import com.halo.core_bridge.api.users.model.entity.UserRole;
import com.halo.core_bridge.api.users.repository.UserRoleRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole findByName(String name) {
        return userRoleRepository.findByName(name)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER_ROLE));
    }
}
