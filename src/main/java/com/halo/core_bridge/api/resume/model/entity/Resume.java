package com.halo.core_bridge.api.resume.model.entity;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Resume extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime applied_at;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OverseasExperience> overseasExperiences = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeSkill> resumeSkills = new ArrayList<>();

    public void addCareer(Career career) {
        this.careers.add(career);
        career.setResume(this);
    }

    public void addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setResume(this);
    }

    public void addEducation(Education education) {
        this.educations.add(education);
        education.setResume(this);
    }

    public void addLanguage(Language language) {
        this.languages.add(language);
        language.setResume(this);
    }

    public void addOverseasExperience(OverseasExperience overseasExperience) {
        this.overseasExperiences.add(overseasExperience);
        overseasExperience.setResume(this);
    }

    public void addResumeSkill(ResumeSkill resumeSkill) {
        this.resumeSkills.add(resumeSkill);
        resumeSkill.setResume(this);
    }
    
    public void updateDescription(String description) {
        this.description = description;
    }
}