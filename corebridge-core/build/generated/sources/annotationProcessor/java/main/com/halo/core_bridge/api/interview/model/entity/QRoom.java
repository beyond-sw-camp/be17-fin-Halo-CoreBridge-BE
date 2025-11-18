package com.halo.core_bridge.api.interview.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = 1694941238L;

    public static final QRoom room = new QRoom("room");

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.halo.core_bridge.api.interview.model.enums.InterviewType> interviewType = createEnum("interviewType", com.halo.core_bridge.api.interview.model.enums.InterviewType.class);

    public final StringPath location = createString("location");

    public final StringPath name = createString("name");

    public QRoom(String variable) {
        super(Room.class, forVariable(variable));
    }

    public QRoom(Path<? extends Room> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoom(PathMetadata metadata) {
        super(Room.class, metadata);
    }

}

