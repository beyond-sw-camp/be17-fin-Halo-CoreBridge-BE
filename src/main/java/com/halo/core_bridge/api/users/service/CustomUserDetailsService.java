package com.halo.core_bridge.api.users.service;

import com.halo.core_bridge.api.users.model.dto.UserDto;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> findUser = userRepository.findByEmail(username);

        if (findUser.isPresent()) {

            User user = findUser.get();

            return UserDto.Auth.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .name(user.getName())
                    .role(user.getUserRole().getCode())
                    .build();
        }

        throw new UsernameNotFoundException(BaseResponseStatus.INVALID_USER_INFO.getMessage());
    }
}
