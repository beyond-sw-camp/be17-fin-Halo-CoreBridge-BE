package com.halo.core_bridge.api.board.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import com.halo.core_bridge.api.users.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Long likeCount;
    private Long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
