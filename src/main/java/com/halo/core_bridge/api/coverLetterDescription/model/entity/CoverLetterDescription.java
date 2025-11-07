package com.halo.core_bridge.api.coverLetterDescription.model.entity;


import com.halo.core_bridge.api.coverLetterTitle.model.entity.CoverLetterTitle;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoverLetterDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne
    private CoverLetterTitle coverLetterTitle;

    @ManyToOne
    private Resume resume;



    @Builder
    public CoverLetterDescription(CoverLetterTitle coverLetterTitle, Resume resume, String description) {
        this.coverLetterTitle = coverLetterTitle;
        this.resume = resume;
        this.description = description;
    }
}
