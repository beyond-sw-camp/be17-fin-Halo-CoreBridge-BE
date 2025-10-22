package com.halo.core_bridge.api.auth.service;

import com.halo.core_bridge.api.auth.model.AuthDto;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.api.users.service.PasswordService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFindService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordService passwordService;

    /**
     * 비밀번호를 초기화 한다.
     * @param resetPassword 비밀번호 재설정 DTO
     */
    @Transactional
    public void resetPassword(AuthDto.ResetPassword resetPassword) {
        String newPassword = resetPassword.getPassword();

        authService.verifyUserPasswordReset(resetPassword);

        Optional<User> result = userRepository.findByEmail(resetPassword.getEmail());

        if (result.isEmpty()) {
            throw BaseException.from(BaseResponseStatus.NOT_FOUND_USER);
        }

        User findUser = result.get();
        passwordService.changePassword(findUser, newPassword);
    }

    public AuthDto.FindEmailResp findEmailByNameAndPhoneNumber(AuthDto.FindEmailReq findEmailInfo) {
        Optional<User> result = userRepository.findByNameAndPhone(findEmailInfo.getName(), findEmailInfo.getPhone());

        if (result.isPresent()) {
            User findUser = result.get();
            return AuthDto.FindEmailResp.from(findUser);
        }

        throw BaseException.from(BaseResponseStatus.NOT_FOUND_USER);
    }
}
