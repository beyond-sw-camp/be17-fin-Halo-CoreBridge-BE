package com.halo.core_bridge.api.personal.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonalData is a Querydsl query type for PersonalData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalData extends EntityPathBase<PersonalData> {

    private static final long serialVersionUID = -1693056736L;

    public static final QPersonalData personalData = new QPersonalData("personalData");

    public final com.halo.core_bridge.common.model.QBaseEntity _super = new com.halo.core_bridge.common.model.QBaseEntity(this);

    public final NumberPath<Double> bonus = createNumber("bonus", Double.class);

    public final NumberPath<Double> cosineSimilarity = createNumber("cosineSimilarity", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath recommendResults = createString("recommendResults");

    public final NumberPath<Long> resumeId = createNumber("resumeId", Long.class);

    public final NumberPath<Double> simScore = createNumber("simScore", Double.class);

    public final NumberPath<Double> skillRatio = createNumber("skillRatio", Double.class);

    public final StringPath skills = createString("skills");

    public final NumberPath<Double> skillScore = createNumber("skillScore", Double.class);

    public final StringPath summary = createString("summary");

    public final NumberPath<Double> totalScore = createNumber("totalScore", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QPersonalData(String variable) {
        super(PersonalData.class, forVariable(variable));
    }

    public QPersonalData(Path<? extends PersonalData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonalData(PathMetadata metadata) {
        super(PersonalData.class, metadata);
    }

}

