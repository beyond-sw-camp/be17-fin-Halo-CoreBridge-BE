package com.halo.core_bridge.api.jobposting.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.interview.model.entity.Interviewer;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "job_posting",
        indexes = {
                @Index(name = "idx_job_posting_title", columnList = "title"),
                @Index(name = "idx_job_posting_careerType", columnList = "career_type"),
                @Index(name = "idx_job_posting_created_at", columnList = "created_at")
        }
)
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본 정보

    private String title;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType; //고용 형태(정규직, 계약직, 인턴)

    @Enumerated(EnumType.STRING)
    private CareerType careerType; //경력 조건(신입, 경력 무관 등)
    private Integer minExperience; //경력으로 등록시 입력 그렇지 않으면 null
    private Integer maxExperience;



    private String positionLevel;
    private String location;

    //날짜 관련
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hireEndDate;

    //모집 관련
    private Integer headcount;


    //직무상세
    @Column(length = 1000)
    private String summary; // 직무 소개

    @Column(length = 1000)
    private String responsibilities; // 주요 업무

    @Column(length = 1000)
    private String requirements; // 필수 자격 요건

    @Column(length = 1000)
    private String preferred; // 우대 요건

    //급여 관련
    @Enumerated(EnumType.STRING)
    private SalaryType salaryType; // 급여 형태

    private Integer salaryMin;
    private Integer salaryMax;

    @Column(
            columnDefinition = "TINYINT(1)"
    )
    private Boolean salaryNegotiable;

    //근무 조건
    private String workingHours;

    @Column(length = 1000)
    private String benefits; //복리 후생

    //담당자 정보
    private String contactName;
    private String contactEmail;

    @Column(length = 1000)
    private String additionalInfo; //기타 안내사항

    //연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(
            mappedBy = "jobPosting",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JobPostingSkill> skills = new ArrayList<>(); // 기술 스택

    @OneToMany(
            mappedBy = "jobPosting",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecruitProcess> recruitProcesses = new ArrayList<>(); //채용 프로세스

    @OneToMany(
            mappedBy = "jobPosting",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Resume> resumes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user_id")
    private User createdUser; //등록한 유저

    @OneToMany(mappedBy = "jobPosting")
    private List<Interviewer> interviewers = new ArrayList<>(); //면접관

}
