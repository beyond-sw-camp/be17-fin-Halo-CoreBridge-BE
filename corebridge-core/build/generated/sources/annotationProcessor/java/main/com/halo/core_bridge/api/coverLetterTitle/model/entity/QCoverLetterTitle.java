package com.halo.core_bridge.api.coverLetterTitle.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoverLetterTitle is a Querydsl query type for CoverLetterTitle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoverLetterTitle extends EntityPathBase<CoverLetterTitle> {

    private static final long serialVersionUID = -833189428L;

    public static final QCoverLetterTitle coverLetterTitle = new QCoverLetterTitle("coverLetterTitle");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> jobPostingId = createNumber("jobPostingId", Long.class);

    public final StringPath subtitle = createString("subtitle");

    public final StringPath title = createString("title");

    public QCoverLetterTitle(String variable) {
        super(CoverLetterTitle.class, forVariable(variable));
    }

    public QCoverLetterTitle(Path<? extends CoverLetterTitle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoverLetterTitle(PathMetadata metadata) {
        super(CoverLetterTitle.class, metadata);
    }

}

