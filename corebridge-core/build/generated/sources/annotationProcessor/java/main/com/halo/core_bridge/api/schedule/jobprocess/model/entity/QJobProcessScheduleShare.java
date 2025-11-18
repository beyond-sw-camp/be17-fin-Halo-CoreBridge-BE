package com.halo.core_bridge.api.schedule.jobprocess.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobProcessScheduleShare is a Querydsl query type for JobProcessScheduleShare
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobProcessScheduleShare extends EntityPathBase<JobProcessScheduleShare> {

    private static final long serialVersionUID = -1571224909L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobProcessScheduleShare jobProcessScheduleShare = new QJobProcessScheduleShare("jobProcessScheduleShare");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJobProcessSchedule schedule;

    public final com.halo.core_bridge.api.users.model.entity.QUser user;

    public QJobProcessScheduleShare(String variable) {
        this(JobProcessScheduleShare.class, forVariable(variable), INITS);
    }

    public QJobProcessScheduleShare(Path<? extends JobProcessScheduleShare> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobProcessScheduleShare(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobProcessScheduleShare(PathMetadata metadata, PathInits inits) {
        this(JobProcessScheduleShare.class, metadata, inits);
    }

    public QJobProcessScheduleShare(Class<? extends JobProcessScheduleShare> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new QJobProcessSchedule(forProperty("schedule"), inits.get("schedule")) : null;
        this.user = inits.isInitialized("user") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

