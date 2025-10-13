package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Language;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {

    private Long id;
    private String name;
    private String testName;
    private String languageName;
    private String grade;
    private String speakingLevel;
    private LocalDate testDate;

    private Resume resume;

    public Language toEntity() {
        return Language.builder()
                .id(this.id)
                .name(this.name)
                .testName(this.testName)
                .languageName(this.languageName)
                .grade(this.grade)
                .speakingLevel(this.speakingLevel)
                .testDate(this.testDate)
                .resume(this.resume)
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
                .resume(language.getResume())
                .build();
    }
}