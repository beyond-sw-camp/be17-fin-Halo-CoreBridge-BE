package com.halo.core_bridge.api.admin.service;

import com.halo.core_bridge.api.mail.service.NewAccountPasswordResetMailService;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import com.halo.core_bridge.api.users.repository.UserQueryRepository;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.api.users.service.PasswordService;
import com.halo.core_bridge.api.users.service.UserRoleService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.halo.core_bridge.api.admin.model.AdminDto.AccountList;
import static com.halo.core_bridge.api.admin.model.AdminDto.UserCreate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {
    
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    private final UserRoleService userRoleService;
    private final PasswordService passwordService;
    private final NewAccountPasswordResetMailService newAccountPasswordResetMailService;

    private final int pageSize = 10;

    /**
     * 관리자 권한으로 계정을 추가합니다.
     * @param userCreate 추가할 계졍의 정보
     * @return 추가된 계정의 ID
     * @throws BaseException 이메일이 이미 존재하면 예외 발생
     */
    @Transactional
    public Long save(UserCreate userCreate) {

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

    /**
     * 채용 담당자, 면접관 권한을 가진 계정 목록을 조회하는 기능 <br>
     * @param roleType - 권한 유형, <code>null</code>이면 채용 담당자과 면접관 권한을 모두 가지고 온다.
     * @return 권한 유형에 따른 계정 목록
     */
    @Transactional(readOnly = true)
    public AccountList findAccounts(String roleType, int page, String keyword) {

        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        if (roleType == null) {
            return AccountList.from(userQueryRepository.searchUsers(List.of("채용 담당자", "면접관"), keyword, pageable));
        }

        UserRole findUserRole = userRoleService.findByName(roleType);
        return AccountList.from(userQueryRepository.searchUsers(List.of(findUserRole.getName()), keyword, pageable));
    }

    /**
     * 계정을 삭제하는 기능
     * @param userId 삭제할 계정의 식별자 <code>id</code>
     * @throws BaseException 계정이 존재하지 않으면 예외 발생
     */
    @Transactional
    public void deleteById(Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[ERROR] {}", BaseResponseStatus.NOT_FOUND_USER.getMessage());
                    return BaseException.from(BaseResponseStatus.NOT_FOUND_USER);
                });

        userRepository.delete(findUser);
    }
}
