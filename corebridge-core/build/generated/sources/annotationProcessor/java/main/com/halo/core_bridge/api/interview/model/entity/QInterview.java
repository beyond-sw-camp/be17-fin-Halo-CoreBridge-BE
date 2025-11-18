package com.halo.core_bridge.api.interview.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterview is a Querydsl query type for Interview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterview extends EntityPathBase<Interview> {

    private static final long serialVersionUID = 811856646L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterview interview = new QInterview("interview");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.halo.core_bridge.api.interview.model.enums.InterviewType> interviewType = createEnum("interviewType", com.halo.core_bridge.api.interview.model.enums.InterviewType.class);

    public final StringPath location = createString("location");

    public final com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess recruitProcess;

    public final BooleanPath reminderSent = createBoolean("reminderSent");

    public final DateTimePath<java.time.LocalDateTime> reminderSentAt = createDateTime("reminderSentAt", java.time.LocalDateTime.class);

    public final com.halo.core_bridge.api.resume.model.entity.QResume resume;

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public final EnumPath<com.halo.core_bridge.api.interview.model.enums.InterviewStatus> status = createEnum("status", com.halo.core_bridge.api.interview.model.enums.InterviewStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QInterview(String variable) {
        this(Interview.class, forVariable(variable), INITS);
    }

    public QInterview(Path<? extends Interview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterview(PathMetadata metadata, PathInits inits) {
        this(Interview.class, metadata, inits);
    }

    public QInterview(Class<? extends Interview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recruitProcess = inits.isInitialized("recruitProcess") ? new com.halo.core_bridge.api.jobposting.model.entity.QRecruitProcess(forProperty("recruitProcess"), inits.get("recruitProcess")) : null;
        this.resume = inits.isInitialized("resume") ? new com.halo.core_bridge.api.resume.model.entity.QResume(forProperty("resume"), inits.get("resume")) : null;
    }

}

