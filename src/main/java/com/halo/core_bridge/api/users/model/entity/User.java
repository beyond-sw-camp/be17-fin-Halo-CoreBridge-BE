package com.halo.core_bridge.api.users.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import com.halo.core_bridge.api.board.model.entity.board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String birth;
    private String gender;
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<board> boards;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserRole userRole;
}
