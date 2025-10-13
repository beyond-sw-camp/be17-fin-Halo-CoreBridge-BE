package com.halo.core_bridge.api.users.repository;

import com.halo.core_bridge.api.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("select u from User u join fetch u.userRole ur where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
