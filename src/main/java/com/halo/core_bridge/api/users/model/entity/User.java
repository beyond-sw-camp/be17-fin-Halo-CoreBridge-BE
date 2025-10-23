package com.halo.core_bridge.api.users.model.entity;

import com.halo.core_bridge.api.image.model.entity.Image;
import com.halo.core_bridge.api.users.model.Gender;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = "email")
        }
)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserRole userRole;

    @OneToOne(mappedBy = "user")
    private Image profileImage;

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
