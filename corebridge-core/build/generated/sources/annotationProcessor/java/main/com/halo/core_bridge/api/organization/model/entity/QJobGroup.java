package com.halo.core_bridge.api.organization.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QJobGroup is a Querydsl query type for JobGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobGroup extends EntityPathBase<JobGroup> {

    private static final long serialVersionUID = 1675639435L;

    public static final QJobGroup jobGroup = new QJobGroup("jobGroup");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QJobGroup(String variable) {
        super(JobGroup.class, forVariable(variable));
    }

    public QJobGroup(Path<? extends JobGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJobGroup(PathMetadata metadata) {
        super(JobGroup.class, metadata);
    }

}

