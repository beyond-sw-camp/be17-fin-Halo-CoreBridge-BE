package com.halo.core_bridge.api.jobposting.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecruitProcess is a Querydsl query type for RecruitProcess
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruitProcess extends EntityPathBase<RecruitProcess> {

    private static final long serialVersionUID = 1911906444L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecruitProcess recruitProcess = new QRecruitProcess("recruitProcess");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    public final EnumPath<com.halo.core_bridge.common.model.ColorCode> colorCode = createEnum("colorCode", com.halo.core_bridge.common.model.ColorCode.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJobPosting jobPosting;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> orderIdx = createNumber("orderIdx", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecruitProcess(String variable) {
        this(RecruitProcess.class, forVariable(variable), INITS);
    }

    public QRecruitProcess(Path<? extends RecruitProcess> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecruitProcess(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecruitProcess(PathMetadata metadata, PathInits inits) {
        this(RecruitProcess.class, metadata, inits);
    }

    public QRecruitProcess(Class<? extends RecruitProcess> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jobPosting = inits.isInitialized("jobPosting") ? new QJobPosting(forProperty("jobPosting"), inits.get("jobPosting")) : null;
    }

}

