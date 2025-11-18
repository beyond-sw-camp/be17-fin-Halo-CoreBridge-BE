package com.halo.core_bridge.api.resume.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QResume is a Querydsl query type for Resume
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QResume extends EntityPathBase<Resume> {

    private static final long serialVersionUID = 593012912L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResume resume = new QResume("resume");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> applied_at = createDateTime("applied_at", java.time.LocalDateTime.class);

    public final ListPath<Career, QCareer> careers = this.<Career, QCareer>createList("careers", Career.class, QCareer.class, PathInits.DIRECT2);

    public final ListPath<Certificate, QCertificate> certificates = this.<Certificate, QCertificate>createList("certificates", Certificate.class, QCertificate.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final ListPath<Education, QEducation> educations = this.<Education, QEducation>createList("educations", Education.class, QEducation.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.halo.core_bridge.api.jobposting.model.entity.QJobPosting jobPosting;

    public final ListPath<Language, QLanguage> languages = this.<Language, QLanguage>createList("languages", Language.class, QLanguage.class, PathInits.DIRECT2);

    public final ListPath<OverseasExperience, QOverseasExperience> overseasExperiences = this.<OverseasExperience, QOverseasExperience>createList("overseasExperiences", OverseasExperience.class, QOverseasExperience.class, PathInits.DIRECT2);

    public final ListPath<com.halo.core_bridge.api.pdf.model.entity.Pdf, com.halo.core_bridge.api.pdf.model.entity.QPdf> pdf = this.<com.halo.core_bridge.api.pdf.model.entity.Pdf, com.halo.core_bridge.api.pdf.model.entity.QPdf>createList("pdf", com.halo.core_bridge.api.pdf.model.entity.Pdf.class, com.halo.core_bridge.api.pdf.model.entity.QPdf.class, PathInits.DIRECT2);

    public final com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess process;

    public final ListPath<ResumeSkill, QResumeSkill> resumeSkills = this.<ResumeSkill, QResumeSkill>createList("resumeSkills", ResumeSkill.class, QResumeSkill.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.halo.core_bridge.api.users.model.entity.QUser user;

    public QResume(String variable) {
        this(Resume.class, forVariable(variable), INITS);
    }

    public QResume(Path<? extends Resume> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResume(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResume(PathMetadata metadata, PathInits inits) {
        this(Resume.class, metadata, inits);
    }

    public QResume(Class<? extends Resume> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jobPosting = inits.isInitialized("jobPosting") ? new com.halo.core_bridge.api.jobposting.model.entity.QJobPosting(forProperty("jobPosting"), inits.get("jobPosting")) : null;
        this.process = inits.isInitialized("process") ? new com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess(forProperty("process"), inits.get("process")) : null;
        this.user = inits.isInitialized("user") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

