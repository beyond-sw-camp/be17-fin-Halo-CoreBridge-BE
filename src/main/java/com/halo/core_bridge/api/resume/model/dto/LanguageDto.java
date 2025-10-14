package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Language;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LanguageDto {
    private Long id;
    private String name;
    private String testName;
    private String languageName;
    private String grade;
    private String speakingLevel;
    private LocalDate testDate;

    public Language toEntity(Resume resume) {
        return Language.builder()
                .name(this.name)
                .testName(this.testName)
                .languageName(this.languageName)
                .grade(this.grade)
                .speakingLevel(this.speakingLevel)
                .testDate(this.testDate)
                .resume(resume)
                .build();
    }

    public static LanguageDto from(Language language) {
        return LanguageDto.builder()
                .id(language.getId())
                .name(language.getName())
                .testName(language.getTestName())
                .languageName(language.getLanguageName())
                .grade(language.getGrade())
                .speakingLevel(language.getSpeakingLevel())
                .testDate(language.getTestDate())
                .build();
    }
}