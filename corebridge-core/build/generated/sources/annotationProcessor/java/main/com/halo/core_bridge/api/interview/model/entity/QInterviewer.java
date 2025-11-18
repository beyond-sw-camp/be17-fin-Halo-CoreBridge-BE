package com.halo.core_bridge.api.interview.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterviewer is a Querydsl query type for Interviewer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterviewer extends EntityPathBase<Interviewer> {

    private static final long serialVersionUID = -1489807821L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterviewer interviewer = new QInterviewer("interviewer");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.halo.core_bridge.api.jobposting.model.entity.QJobPosting jobPosting;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.halo.core_bridge.api.users.model.entity.QUser user;

    public QInterviewer(String variable) {
        this(Interviewer.class, forVariable(variable), INITS);
    }

    public QInterviewer(Path<? extends Interviewer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterviewer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterviewer(PathMetadata metadata, PathInits inits) {
        this(Interviewer.class, metadata, inits);
    }

    public QInterviewer(Class<? extends Interviewer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jobPosting = inits.isInitialized("jobPosting") ? new com.halo.core_bridge.api.jobposting.model.entity.QJobPosting(forProperty("jobPosting"), inits.get("jobPosting")) : null;
        this.user = inits.isInitialized("user") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

