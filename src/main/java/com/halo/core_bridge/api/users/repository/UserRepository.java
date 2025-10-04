package com.halo.core_bridge.api.users.repository;

import com.halo.core_bridge.api.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
