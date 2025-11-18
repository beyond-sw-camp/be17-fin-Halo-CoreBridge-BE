package com.halo.core_bridge.api.coverLetterTitle.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cover_letter_title")
public class CoverLetterTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String subtitle;
    private Long jobPostingId;



    @Builder
    public CoverLetterTitle(String title, String subtitle, Long jobPostingId) {
        this.title = title;
        this.subtitle = subtitle;
        this.jobPostingId = jobPostingId;
    }
}