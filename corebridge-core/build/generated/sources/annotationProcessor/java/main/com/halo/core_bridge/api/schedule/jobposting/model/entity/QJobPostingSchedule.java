package com.halo.core_bridge.api.schedule.jobposting.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobPostingSchedule is a Querydsl query type for JobPostingSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobPostingSchedule extends EntityPathBase<JobPostingSchedule> {

    private static final long serialVersionUID = -275579086L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobPostingSchedule jobPostingSchedule = new QJobPostingSchedule("jobPostingSchedule");

    public final NumberPath<Integer> applicants = createNumber("applicants", Integer.class);

    public final com.halo.core_bridge.api.users.model.entity.QUser assignedTo;

    public final StringPath benefits = createString("benefits");

    public final DatePath<java.time.LocalDate> deadline = createDate("deadline", java.time.LocalDate.class);

    public final StringPath department = createString("department");

    public final StringPath description = createString("description");

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final StringPath experience = createString("experience");

    public final NumberPath<Integer> finalStage = createNumber("finalStage", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> interview1 = createNumber("interview1", Integer.class);

    public final NumberPath<Integer> interview2 = createNumber("interview2", Integer.class);

    public final StringPath position = createString("position");

    public final DatePath<java.time.LocalDate> postedDate = createDate("postedDate", java.time.LocalDate.class);

    public final StringPath preferences = createString("preferences");

    public final NumberPath<Integer> progress = createNumber("progress", Integer.class);

    public final StringPath requirements = createString("requirements");

    public final StringPath responsibilities = createString("responsibilities");

    public final NumberPath<Integer> screening = createNumber("screening", Integer.class);

    public final SetPath<JobPostingScheduleShare, QJobPostingScheduleShare> shares = this.<JobPostingScheduleShare, QJobPostingScheduleShare>createSet("shares", JobPostingScheduleShare.class, QJobPostingScheduleShare.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final EnumPath<com.halo.core_bridge.api.schedule.jobposting.model.enums.JobPostingStatus> status = createEnum("status", com.halo.core_bridge.api.schedule.jobposting.model.enums.JobPostingStatus.class);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final BooleanPath urgent = createBoolean("urgent");

    public QJobPostingSchedule(String variable) {
        this(JobPostingSchedule.class, forVariable(variable), INITS);
    }

    public QJobPostingSchedule(Path<? extends JobPostingSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobPostingSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobPostingSchedule(PathMetadata metadata, PathInits inits) {
        this(JobPostingSchedule.class, metadata, inits);
    }

    public QJobPostingSchedule(Class<? extends JobPostingSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assignedTo = inits.isInitialized("assignedTo") ? new com.halo.core_bridge.api.users.model.entity.QUser(forProperty("assignedTo"), inits.get("assignedTo")) : null;
    }

}

