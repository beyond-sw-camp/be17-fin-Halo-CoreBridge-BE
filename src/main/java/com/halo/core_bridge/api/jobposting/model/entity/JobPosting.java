package com.halo.core_bridge.api.jobposting.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 2000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hireEndDate;

    @Column(nullable = true)
    private int minExperience;

    @Column(nullable = true)
    private int maxExperience;

    private String employmentType;
    private String careerType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToMany(
            mappedBy = "jobPosting",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,       // 부모 저장/수정 시 자식 함께 반영
            orphanRemoval = true             // 부모에서 제거되면 고아 삭제
    )
    //연관된 기술스택
    private List<JobPostingSkill> skills = new ArrayList<>();

    //연관된 채용 프로세스
    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitProcess> recruitProcesses = new ArrayList<>();

    public void update(JobPostingDto.UpdateRequest dto, Department department) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.employmentType = dto.getEmploymentType();
        this.careerType = dto.getCareerType();
        this.minExperience = dto.getMinExperience();
        this.maxExperience = dto.getMaxExperience();
        this.applyStartDate = dto.getApplyStartDate();
        this.applyEndDate = dto.getApplyEndDate();
        this.hireEndDate = dto.getHireEndDate();
        this.department = department;
    }

    public void updateSkills(List<JobPostingSkill> skills) {
        this.skills.clear();
        this.skills.addAll(skills);
    }
}
