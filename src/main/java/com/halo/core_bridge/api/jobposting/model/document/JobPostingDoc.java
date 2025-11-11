package com.halo.core_bridge.api.jobposting.model.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "job-postings")
public class JobPostingDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "title", analyzer = "")
    private String title;

    @Field(type = FieldType.Keyword, name = "department_name")
    private String departmentName;

    @Field(type = FieldType.Keyword, name = "employment_type")
    private String employmentType;

    @Field(type = FieldType.Keyword, name = "career_type")
    private String careerType;

    @Field(type = FieldType.Text, name = "summary_text")
    private String summaryText;

    @Field(type = FieldType.Keyword, name = "status")
    private String status;

    @Field(type = FieldType.Date, name = "hire_end_date")
    private LocalDateTime hireEndDate;

    @Field(type = FieldType.Keyword, name = "dday")
    private String dday;

    @Field(type = FieldType.Integer, name = "applicant_count")
    private Integer applicantCount;

    @Field(type = FieldType.Integer, name = "progress_percent")
    private Integer progressPercent;
}
