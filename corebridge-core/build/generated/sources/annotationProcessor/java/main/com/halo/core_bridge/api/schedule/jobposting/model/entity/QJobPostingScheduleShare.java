package com.halo.core_bridge.api.schedule.jobposting.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobPostingScheduleShare is a Querydsl query type for JobPostingScheduleShare
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobPostingScheduleShare extends EntityPathBase<JobPostingScheduleShare> {

    private static final long serialVersionUID = 2039025613L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobPostingScheduleShare jobPostingScheduleShare = new QJobPostingScheduleShare("jobPostingScheduleShare");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJobPostingSchedule schedule;

    public final com.halo.core_bridge.api.users.model.entity.QUser user;

    public QJobPostingScheduleShare(String variable) {
        this(JobPostingScheduleShare.class, forVariable(variable), INITS);
    }

    public QJobPostingScheduleShare(Path<? extends JobPostingScheduleShare> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobPostingScheduleShare(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobPostingScheduleShare(PathMetadata metadata, PathInits inits) {
        this(JobPostingScheduleShare.class, metadata, inits);
    }

    public QJobPostingScheduleShare(Class<? extends JobPostingScheduleShare> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new QJobPostingSchedule(forProperty("schedule"), inits.get("schedule")) : null;
        this.user = inits.isInitialized("user") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

