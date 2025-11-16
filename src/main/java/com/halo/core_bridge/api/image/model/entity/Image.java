package com.halo.core_bridge.api.image.model.entity;

import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFilename;
    private String savedPath;
    private String contentType;
    private Long fileSize;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Builder
    public Image(String originalFilename, String savedPath, String contentType, Long fileSize, Long userIdx) {
        this.originalFilename = originalFilename;
        this.savedPath = savedPath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.isDeleted = false;
    }

    public void safeDelete() {
        this.isDeleted = true;
    }
}