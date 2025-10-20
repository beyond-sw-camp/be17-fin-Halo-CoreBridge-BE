package com.halo.core_bridge.api.users.service;

import com.halo.core_bridge.api.users.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public void encodePassword(User entity, String password) {
        String encodedPassword = passwordEncoder.encode(password);

        entity.updatePassword(encodedPassword);
    }

    public void changePassword(User entity, String newPassword) {
        this.encodePassword(entity, newPassword);
    }
}