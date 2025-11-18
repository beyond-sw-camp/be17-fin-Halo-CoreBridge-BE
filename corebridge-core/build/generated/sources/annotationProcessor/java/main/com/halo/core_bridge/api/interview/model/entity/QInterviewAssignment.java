package com.halo.core_bridge.api.interview.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterviewAssignment is a Querydsl query type for InterviewAssignment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterviewAssignment extends EntityPathBase<InterviewAssignment> {

    private static final long serialVersionUID = -1750481293L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterviewAssignment interviewAssignment = new QInterviewAssignment("interviewAssignment");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInterview interview;

    public final com.halo.core_bridge.api.users.model.entity.QUser interviewer;

    public QInterviewAssignment(String variable) {
        this(InterviewAssignment.class, forVariable(variable), INITS);
    }

    public QInterviewAssignment(Path<? extends InterviewAssignment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterviewAssignment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterviewAssignment(PathMetadata metadata, PathInits inits) {
        this(InterviewAssignment.class, metadata, inits);
    }

    public QInterviewAssignment(Class<? extends InterviewAssignment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interview = inits.isInitialized("interview") ? new QInterview(forProperty("interview"), inits.get("interview")) : null;
        this.interviewer = inits.isInitialized("interviewer") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("interviewer"), inits.get("interviewer")) : null;
    }

}

