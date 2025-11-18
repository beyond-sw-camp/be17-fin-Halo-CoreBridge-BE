package com.halo.core_bridge.api.organization.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDuty is a Querydsl query type for Duty
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDuty extends EntityPathBase<Duty> {

    private static final long serialVersionUID = 49842463L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDuty duty = new QDuty("duty");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJobGroup jobGroup;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDuty(String variable) {
        this(Duty.class, forVariable(variable), INITS);
    }

    public QDuty(Path<? extends Duty> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDuty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDuty(PathMetadata metadata, PathInits inits) {
        this(Duty.class, metadata, inits);
    }

    public QDuty(Class<? extends Duty> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jobGroup = inits.isInitialized("jobGroup") ? new QJobGroup(forProperty("jobGroup")) : null;
    }

}

