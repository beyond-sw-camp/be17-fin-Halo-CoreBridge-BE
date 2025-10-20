package com.halo.core_bridge.api.auth.service;

import com.halo.core_bridge.api.auth.model.AuthDto;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.api.users.service.PasswordService;
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
     * @param resetPassword
     */
    @Transactional
    public void resetPassword(AuthDto.ResetPassword resetPassword) {
        String newPassword = resetPassword.getPassword();

        String token = resetPassword.getToken();

        AuthDto.ResetPassword findEmail = authService.verifyUserPasswordReset(token);

        Optional<User> result = userRepository.findByEmail(findEmail.getEmail());

        if (result.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 이메일");
        }

        User findUser = result.get();
        passwordService.changePassword(findUser, newPassword);
    }
}
