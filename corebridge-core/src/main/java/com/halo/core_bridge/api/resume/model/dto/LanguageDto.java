package com.halo.core_bridge.api.resume.model.dto;

import com.halo.core_bridge.api.resume.model.entity.Language;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LanguageDto {
    @Schema(description = "어학 ID", example = "1")
    private Long id;
    @Schema(description = "어학 시험명", example = "TOEIC")
    private String name;
    @Schema(description = "시험 종류", example = "TOEIC")
    private String testName;
    @Schema(description = "언어명", example = "영어")
    private String languageName;
    @Schema(description = "등급/점수", example = "900")
    private String grade;
    @Schema(description = "회화 수준", example = "상")
    private String speakingLevel;
    @Schema(description = "시험일", example = "2021-03-15")
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