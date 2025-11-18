package com.halo.core_bridge.api.users.service;

import com.halo.core_bridge.api.admin.model.AdminDto;
import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.model.entity.UserRole;
import com.halo.core_bridge.api.users.repository.UserQueryRepository;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final UserQueryRepository userQueryRepository;
    private final UserRoleService userRoleService;

    public Long save(UserDto.Create createUser) {
        try {

            User userEntity = createUser.toEntity();
            passwordService.encodePassword(userEntity, createUser.getPassword());

            User savedUser = userRepository.save(userEntity);
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

    /**
     * 이메일 중복을 검사한다.
     * @param email 중복인지 확인할 이메일
     * @throws BaseException 이메일 중복 예외 발생
     */
    public void existByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw BaseException.from(BaseResponseStatus.DUPLICATE_USER_EMAIL);
        }
    }

    /**
     * 이력서 작성 초기 데이터 구성에 필요한 지원자 정보를 조회 한다.
     * @param userId 지원자의 식별자 ID
     * @return UserDto.ResumeUserInfo 지원서 작성에 필요한 지원자 정보를 담은 DTO
     * @throws BaseException - 회원이 존재하지 않는 경우 에외가 발생한다.
     */
    public User findForResumeInfo(Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(
                () -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER)
        );

        return findUser;
    }

    /**
     * 사용자 정보를 상세 조회 한다.
     * @param userId 조회할 사용자 ID
     * @return <code>UserDto.Read</code> 사용저 정보 조회 DTO
     */
    public UserDto.Read findById(Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(
                () -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER)
        );

        return UserDto.Read.from(findUser);
    }

    /**
     * 계정을 무한 스크롤로 조회하는 기능
     * @param roleType 권한 유형, <code>null</code>이면 채용 담당자과 면접관 권한을 모두 가지고 온다.
     * @param page 페이지 번호
     * @param keyword 검색어
     * @return AccountInfiniteList 계정 목록
     */
    @Transactional(readOnly = true)
    public AdminDto.AccountInfiniteList findInfiniteAccounts(String roleType, int page, String keyword) {

        PageRequest pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        if (roleType == null) {
            return AdminDto.AccountInfiniteList.from(userQueryRepository.findInterviewers(List.of("채용 담당자", "면접관"), keyword, pageable));
        }

        UserRole findUserRole = userRoleService.findByName(roleType);
        return AdminDto.AccountInfiniteList.from(userQueryRepository.findInterviewers(List.of(findUserRole.getName()), keyword, pageable));
    }
}
