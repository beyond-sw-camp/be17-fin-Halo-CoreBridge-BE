package com.halo.core_bridge.api.jobposting.model.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "recruit-process")
public class RecruitProcessDoc {

    @Id
    @Field(type = FieldType.Text, name = "process_id")
    private Long processId;

    @Field(type = FieldType.Long, name = "job_posting_id")
    private Long jobPostingId;

    @Field(type = FieldType.Text, name = "job_posting_title")
    private String jobPostingTitle;

    @Field(type = FieldType.Keyword, name = "stage_name")
    private String stageName;

    @Field(type = FieldType.Integer, name = "order_index")
    private Integer orderIndex;

    @Field(type = FieldType.Integer, name = "count")
    private Integer count;
}