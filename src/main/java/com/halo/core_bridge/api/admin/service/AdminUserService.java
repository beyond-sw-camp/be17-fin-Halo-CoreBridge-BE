package com.halo.core_bridge.api.admin.service;

import com.halo.core_bridge.api.admin.model.AdminDto;
import com.halo.core_bridge.api.mail.service.NewAccountPasswordResetMailService;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.api.users.service.PasswordService;
import com.halo.core_bridge.api.users.service.UserRoleService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    
    private final UserRepository userRepository;

    private final UserRoleService userRoleService;
    private final PasswordService passwordService;
    private final NewAccountPasswordResetMailService newAccountPasswordResetMailService;

    /**
     * 관리자 권한으로 계정을 추가합니다.
     * @param userCreate 추가할 계졍의 정보
     * @return 추가된 계정의 ID
     * @throws BaseException 이메일이 이미 존재하면 예외 발생
     */
    @Transactional
    public Long save(AdminDto.UserCreate userCreate) {

        if (userRepository.existsByEmail(userCreate.getEmail())) {
            throw BaseException.from(BaseResponseStatus.DUPLICATE_USER_EMAIL);
        }

        UserRole findUserRole = userRoleService.findByName(userCreate.getRoleType());

        User createUserEntity = userCreate.toEntity(findUserRole);

        String tmpPassword = UUID.randomUUID().toString();
        passwordService.encodePassword(createUserEntity, tmpPassword);
        passwordService.changePassword(createUserEntity, tmpPassword);

        User savedUser = userRepository.save(createUserEntity);

        // 비밀번호 변경 링크 이메일 전송
        newAccountPasswordResetMailService.sendToEmail(savedUser.getEmail());

        return savedUser.getId();
    }
}
