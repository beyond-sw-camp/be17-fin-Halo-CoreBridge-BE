package com.halo.core_bridge.api.jobposting.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobPosting is a Querydsl query type for JobPosting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobPosting extends EntityPathBase<JobPosting> {

    private static final long serialVersionUID = -1750236800L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobPosting jobPosting = new QJobPosting("jobPosting");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    public final StringPath additionalInfo = createString("additionalInfo");

    public final DateTimePath<java.time.LocalDateTime> applyEndDate = createDateTime("applyEndDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> applyStartDate = createDateTime("applyStartDate", java.time.LocalDateTime.class);

    public final StringPath benefits = createString("benefits");

    public final EnumPath<CareerType> careerType = createEnum("careerType", CareerType.class);

    public final StringPath contactEmail = createString("contactEmail");

    public final StringPath contactName = createString("contactName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.halo.core_bridge.api.users.model.entity.QUser createdUser;

    public final com.halo.core_bridge.api.organization.model.entity.QDepartment department;

    public final EnumPath<EmploymentType> employmentType = createEnum("employmentType", EmploymentType.class);

    public final NumberPath<Integer> headcount = createNumber("headcount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> hireEndDate = createDateTime("hireEndDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.halo.core_bridge.api.interview.model.entity.Interviewer, com.halo.core_bridge.api.interview.model.entity.QInterviewer> interviewers = this.<com.halo.core_bridge.api.interview.model.entity.Interviewer, com.halo.core_bridge.api.interview.model.entity.QInterviewer>createList("interviewers", com.halo.core_bridge.api.interview.model.entity.Interviewer.class, com.halo.core_bridge.api.interview.model.entity.QInterviewer.class, PathInits.DIRECT2);

    public final StringPath location = createString("location");

    public final NumberPath<Integer> maxExperience = createNumber("maxExperience", Integer.class);

    public final NumberPath<Integer> minExperience = createNumber("minExperience", Integer.class);

    public final StringPath positionLevel = createString("positionLevel");

    public final StringPath preferred = createString("preferred");

    public final ListPath<RecruitProcess, QRecruitProcess> recruitProcesses = this.<RecruitProcess, QRecruitProcess>createList("recruitProcesses", RecruitProcess.class, QRecruitProcess.class, PathInits.DIRECT2);

    public final StringPath requirements = createString("requirements");

    public final StringPath responsibilities = createString("responsibilities");

    public final ListPath<com.halo.core_bridge.api.resume.model.entity.Resume, com.halo.core_bridge.api.resume.model.entity.QResume> resumes = this.<com.halo.core_bridge.api.resume.model.entity.Resume, com.halo.core_bridge.api.resume.model.entity.QResume>createList("resumes", com.halo.core_bridge.api.resume.model.entity.Resume.class, com.halo.core_bridge.api.resume.model.entity.QResume.class, PathInits.DIRECT2);

    public final NumberPath<Integer> salaryMax = createNumber("salaryMax", Integer.class);

    public final NumberPath<Integer> salaryMin = createNumber("salaryMin", Integer.class);

    public final BooleanPath salaryNegotiable = createBoolean("salaryNegotiable");

    public final EnumPath<SalaryType> salaryType = createEnum("salaryType", SalaryType.class);

    public final ListPath<JobPostingSkill, QJobPostingSkill> skills = this.<JobPostingSkill, QJobPostingSkill>createList("skills", JobPostingSkill.class, QJobPostingSkill.class, PathInits.DIRECT2);

    public final StringPath summary = createString("summary");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath workingHours = createString("workingHours");

    public QJobPosting(String variable) {
        this(JobPosting.class, forVariable(variable), INITS);
    }

    public QJobPosting(Path<? extends JobPosting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobPosting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobPosting(PathMetadata metadata, PathInits inits) {
        this(JobPosting.class, metadata, inits);
    }

    public QJobPosting(Class<? extends JobPosting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdUser = inits.isInitialized("createdUser") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("createdUser"), inits.get("createdUser")) : null;
        this.department = inits.isInitialized("department") ? new com.halo.core_bridge.api.organization.model.entity.QDepartment(forProperty("department"), inits.get("department")) : null;
    }

}

