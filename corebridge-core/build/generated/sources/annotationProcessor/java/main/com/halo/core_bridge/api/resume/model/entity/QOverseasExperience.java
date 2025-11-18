package com.halo.core_bridge.api.resume.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOverseasExperience is a Querydsl query type for OverseasExperience
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOverseasExperience extends EntityPathBase<OverseasExperience> {

    private static final long serialVersionUID = 162380901L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOverseasExperience overseasExperience = new QOverseasExperience("overseasExperience");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    public final StringPath country = createString("country");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath note = createString("note");

    public final QResume resume;

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath type = createString("type");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOverseasExperience(String variable) {
        this(OverseasExperience.class, forVariable(variable), INITS);
    }

    public QOverseasExperience(Path<? extends OverseasExperience> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOverseasExperience(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOverseasExperience(PathMetadata metadata, PathInits inits) {
        this(OverseasExperience.class, metadata, inits);
    }

    public QOverseasExperience(Class<? extends OverseasExperience> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.resume = inits.isInitialized("resume") ? new QResume(forProperty("resume"), inits.get("resume")) : null;
    }

}

