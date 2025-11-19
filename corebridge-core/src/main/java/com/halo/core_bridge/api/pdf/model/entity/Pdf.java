package com.halo.core_bridge.api.pdf.model.entity;

import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pdf extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFilename;
    private String savedPath;
    private String contentType;
    private Long fileSize;
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", unique = true)
    private Resume resume;

    @Builder
    public Pdf(String originalFilename, String savedPath, String contentType, Long fileSize, Resume resume) {
        this.originalFilename = originalFilename;
        this.savedPath = savedPath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.resume = resume;
        this.isDeleted = false;
    }

    public void safeDelete() {
        this.isDeleted = true;
    }
}