package com.halo.core_bridge.api.pdf.model.entity;

import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Pdf extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String savedPath;
    private Long fileSize;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private Long resumeId;

    @Builder
    public Pdf(String originalFilename, String savedPath, Long fileSize, Long resumeId) {
        this.originalFilename = originalFilename;
        this.savedPath = savedPath;
        this.fileSize = fileSize;
        this.isDeleted = false;
        this.resumeId = resumeId;
    }

    public void safeDelete() {
        this.isDeleted = true;
    }
}