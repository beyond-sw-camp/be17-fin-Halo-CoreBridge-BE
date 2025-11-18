package com.halo.core_bridge.api.coverLetterDescription.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoverLetterDescription is a Querydsl query type for CoverLetterDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoverLetterDescription extends EntityPathBase<CoverLetterDescription> {

    private static final long serialVersionUID = 330891092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoverLetterDescription coverLetterDescription = new QCoverLetterDescription("coverLetterDescription");

    public final com.halo.core_bridge.api.coverLetterTitle.model.entity.QCoverLetterTitle coverLetterTitle;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.halo.core_bridge.api.resume.model.entity.QResume resume;

    public QCoverLetterDescription(String variable) {
        this(CoverLetterDescription.class, forVariable(variable), INITS);
    }

    public QCoverLetterDescription(Path<? extends CoverLetterDescription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoverLetterDescription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoverLetterDescription(PathMetadata metadata, PathInits inits) {
        this(CoverLetterDescription.class, metadata, inits);
    }

    public QCoverLetterDescription(Class<? extends CoverLetterDescription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coverLetterTitle = inits.isInitialized("coverLetterTitle") ? new com.halo.core_bridge.api.coverLetterTitle.model.entity.QCoverLetterTitle(forProperty("coverLetterTitle")) : null;
        this.resume = inits.isInitialized("resume") ? new com.halo.core_bridge.api.resume.model.entity.QResume(forProperty("resume"), inits.get("resume")) : null;
    }

}

