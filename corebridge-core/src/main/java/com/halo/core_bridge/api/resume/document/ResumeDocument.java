package com.halo.core_bridge.api.resume.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "resumes")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Date)
    private String appliedAt;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String userName;

    @Field(type = FieldType.Keyword)
    private String userEmail;

    @Field(type = FieldType.Keyword)
    private String userPhone;

    @Field(type = FieldType.Long)
    private Long jobPostingId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String jobPostingTitle;

    @Field(type = FieldType.Nested)
    private List<CareerInfo> careers;

    @Field(type = FieldType.Nested)
    private List<CertificateInfo> certificates;

    @Field(type = FieldType.Nested)
    private List<EducationInfo> educations;

    @Field(type = FieldType.Nested)
    private List<LanguageInfo> languages;

    @Field(type = FieldType.Nested)
    private List<OverseasExperienceInfo> overseasExperiences;

    @Field(type = FieldType.Keyword)
    private List<String> skills;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String allText;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerInfo {
        @Field(type = FieldType.Text, analyzer = "nori")
        private String companyName;

        @Field(type = FieldType.Text, analyzer = "nori")
        private String position;

        @Field(type = FieldType.Date)
        private String startDate;

        @Field(type = FieldType.Date)
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CertificateInfo {
        @Field(type = FieldType.Text, analyzer = "nori")
        private String name;

        @Field(type = FieldType.Keyword)
        private String acquiredDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationInfo {
        @Field(type = FieldType.Text, analyzer = "nori")
        private String schoolName;

        @Field(type = FieldType.Text, analyzer = "nori")
        private String major;

        @Field(type = FieldType.Keyword)
        private String degree;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LanguageInfo {
        @Field(type = FieldType.Keyword)
        private String name;

        @Field(type = FieldType.Keyword)
        private String testName;

        @Field(type = FieldType.Keyword)
        private String languageName;

        @Field(type = FieldType.Keyword)
        private String grade;

        @Field(type = FieldType.Keyword)
        private String speakingLevel;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverseasExperienceInfo {
        @Field(type = FieldType.Keyword)
        private String type;

        @Field(type = FieldType.Keyword)
        private String country;

        @Field(type = FieldType.Text, analyzer = "nori")
        private String note;
    }
}