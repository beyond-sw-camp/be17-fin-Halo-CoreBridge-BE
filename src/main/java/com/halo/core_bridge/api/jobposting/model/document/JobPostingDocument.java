package com.halo.core_bridge.api.jobposting.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.halo.core_bridge.api.jobposting.model.entity.CareerType;
import com.halo.core_bridge.api.jobposting.model.entity.EmploymentType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "job_postings")
public class JobPostingDocument {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @JsonProperty("@timestamp")
    @Field(name = "@timestamp", type = FieldType.Date)
    private String timestamp;

    @Field(name = "summary", type = FieldType.Text)
    private String summary;

    @Field(name = "requirements", type = FieldType.Text)
    private String requirements;

    @Field(name = "preferred", type = FieldType.Text)
    private String preferred;

    @Field(name = "responsibilities", type = FieldType.Text)
    private String responsibilities;

    @Field(name = "department_name", type = FieldType.Text)
    private String departmentName;

    @Field(name = "apply_start_date", type = FieldType.Date)
    private LocalDateTime applyStartDate;

    @Field(name = "apply_end_date", type = FieldType.Date)
    private LocalDateTime applyEndDate;

    @Field(name = "updated_at", type = FieldType.Date)
    private LocalDateTime updatedAt;

    @Field(name = "applicant_count", type = FieldType.Long)
    private Long applicantCount;

    @Field(name = "d_day", type = FieldType.Keyword)
    private String dDay;

    @Field(name = "progress_rate", type = FieldType.Long)
    private Integer progressRate;

    @Field(name = "status", type = FieldType.Text)
    private String status;

    @Field(name = "processes", type = FieldType.Nested)
    private List<ProcessInfo> processes;

    @Field(name = "career_type", type = FieldType.Keyword)
    private String careerType;

    @Field(name = "employment_type", type = FieldType.Keyword)
    private String employmentType;

    public CareerType getCareerType() {
        return CareerType.valueOf(careerType);
    }

    public EmploymentType getEmploymentType() {
        return EmploymentType.valueOf(employmentType);
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessInfo {

        @Field(name = "name", type = FieldType.Text)
        private String name;

        @Field(name = "order_idx", type = FieldType.Long)
        private Integer orderIdx;

        @Field(name = "applicant_count", type = FieldType.Long)
        private Long applicantCount;
    }

}