package com.halo.core_bridge.api.jobposting.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobPostingSkill is a Querydsl query type for JobPostingSkill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobPostingSkill extends EntityPathBase<JobPostingSkill> {

    private static final long serialVersionUID = 751519921L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobPostingSkill jobPostingSkill = new QJobPostingSkill("jobPostingSkill");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJobPosting jobPosting;

    public final EnumPath<TechStack> name = createEnum("name", TechStack.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QJobPostingSkill(String variable) {
        this(JobPostingSkill.class, forVariable(variable), INITS);
    }

    public QJobPostingSkill(Path<? extends JobPostingSkill> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobPostingSkill(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobPostingSkill(PathMetadata metadata, PathInits inits) {
        this(JobPostingSkill.class, metadata, inits);
    }

    public QJobPostingSkill(Class<? extends JobPostingSkill> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jobPosting = inits.isInitialized("jobPosting") ? new QJobPosting(forProperty("jobPosting"), inits.get("jobPosting")) : null;
    }

}

