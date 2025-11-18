package com.halo.core_bridge.api.schedule.jobprocess.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobProcessSchedule is a Querydsl query type for JobProcessSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobProcessSchedule extends EntityPathBase<JobProcessSchedule> {

    private static final long serialVersionUID = 1457804428L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobProcessSchedule jobProcessSchedule = new QJobProcessSchedule("jobProcessSchedule");

    public final com.halo.core_bridge.api.users.model.entity.QUser assignedTo;

    public final StringPath candidateName = createString("candidateName");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath interviewer = createString("interviewer");

    public final NumberPath<Long> jobPostingId = createNumber("jobPostingId", Long.class);

    public final StringPath location = createString("location");

    public final StringPath notes = createString("notes");

    public final NumberPath<Long> parentScheduleId = createNumber("parentScheduleId", Long.class);

    public final StringPath position = createString("position");

    public final StringPath priority = createString("priority");

    public final DatePath<java.time.LocalDate> recurrenceEndDate = createDate("recurrenceEndDate", java.time.LocalDate.class);

    public final NumberPath<Integer> recurrenceInterval = createNumber("recurrenceInterval", Integer.class);

    public final EnumPath<com.halo.core_bridge.api.schedule.jobprocess.model.enums.RecurrenceType> recurrenceType = createEnum("recurrenceType", com.halo.core_bridge.api.schedule.jobprocess.model.enums.RecurrenceType.class);

    public final StringPath scheduleType = createString("scheduleType");

    public final SetPath<JobProcessScheduleShare, QJobProcessScheduleShare> shares = this.<JobProcessScheduleShare, QJobProcessScheduleShare>createSet("shares", JobProcessScheduleShare.class, QJobProcessScheduleShare.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final StringPath status = createString("status");

    public final StringPath title = createString("title");

    public QJobProcessSchedule(String variable) {
        this(JobProcessSchedule.class, forVariable(variable), INITS);
    }

    public QJobProcessSchedule(Path<? extends JobProcessSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobProcessSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobProcessSchedule(PathMetadata metadata, PathInits inits) {
        this(JobProcessSchedule.class, metadata, inits);
    }

    public QJobProcessSchedule(Class<? extends JobProcessSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assignedTo = inits.isInitialized("assignedTo") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("assignedTo"), inits.get("assignedTo")) : null;
    }

}

