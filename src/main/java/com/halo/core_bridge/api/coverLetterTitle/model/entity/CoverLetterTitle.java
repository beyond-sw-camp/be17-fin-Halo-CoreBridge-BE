package com.halo.core_bridge.api.coverLetterTitle.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cover_letter_title")
public class CoverLetterTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String subTitle;
    private Long jobPostingId;



    @Builder
    public CoverLetterTitle(String title, String subTitle, Long jobPostingId) {
        this.title = title;
        this.subTitle = subTitle;
        this.jobPostingId = jobPostingId;
    }
}