package com.halo.core_bridge.api.personal.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonalDataHistory is a Querydsl query type for PersonalDataHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalDataHistory extends EntityPathBase<PersonalDataHistory> {

    private static final long serialVersionUID = 1475671892L;

    public static final QPersonalDataHistory personalDataHistory = new QPersonalDataHistory("personalDataHistory");

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

    public QPersonalDataHistory(String variable) {
        super(PersonalDataHistory.class, forVariable(variable));
    }

    public QPersonalDataHistory(Path<? extends PersonalDataHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonalDataHistory(PathMetadata metadata) {
        super(PersonalDataHistory.class, metadata);
    }

}

