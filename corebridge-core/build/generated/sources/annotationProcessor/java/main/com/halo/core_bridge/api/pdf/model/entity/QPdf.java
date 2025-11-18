package com.halo.core_bridge.api.pdf.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPdf is a Querydsl query type for Pdf
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPdf extends EntityPathBase<Pdf> {

    private static final long serialVersionUID = -1688555162L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPdf pdf = new QPdf("pdf");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath originalFilename = createString("originalFilename");

    public final com.halo.core_bridge.api.resume.model.entity.QResume resume;

    public final StringPath savedPath = createString("savedPath");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPdf(String variable) {
        this(Pdf.class, forVariable(variable), INITS);
    }

    public QPdf(Path<? extends Pdf> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPdf(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPdf(PathMetadata metadata, PathInits inits) {
        this(Pdf.class, metadata, inits);
    }

    public QPdf(Class<? extends Pdf> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.resume = inits.isInitialized("resume") ? new com.halo.core_bridge.api.resume.model.entity.QResume(forProperty("resume"), inits.get("resume")) : null;
    }

}

