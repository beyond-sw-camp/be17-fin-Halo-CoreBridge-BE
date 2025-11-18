package com.halo.core_bridge.api.users.repository;

import com.halo.core_bridge.api.users.model.entity.QUser;
import com.halo.core_bridge.api.users.model.entity.QUserRole;
import com.halo.core_bridge.api.users.model.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUser user = QUser.user;
    private final QUserRole userRole = QUserRole.userRole;

    public Page<User> searchUsers(List<String> roles, String keyword, Pageable pageable) {

        BooleanBuilder condition = getBuilderCondition(roles, keyword);

        List<User> results = jpaQueryFactory
                .selectFrom(user)
                .join(user.userRole, userRole).fetchJoin()
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(user.count())
                .from(user)
                .join(user.userRole, userRole)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);

    }

    public Slice<User> findInterviewers(List<String> roles, String keyword, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();

        if (roles != null && !roles.isEmpty()) {
            condition.and(userRole.name.in(roles));
        }

        // 검색 조건
        if (hasText(keyword)) {
            condition.and(user.name.containsIgnoreCase(keyword));
        }

        // 1. limit + 1 만큼 조회해서 다음 페이지 존재 여부 판단
        List<User> results = jpaQueryFactory
                .selectFrom(user)
                .where(condition)
                .orderBy(user.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 2. hasNext 계산
        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize()); // 초과분 제거
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanBuilder getBuilderCondition(List<String> roles, String keyword) {
        BooleanBuilder condition = new BooleanBuilder();

        // 역할 조건
        if (roles != null && !roles.isEmpty()) {
            condition.and(userRole.name.in(roles));
        }

        // 검색 조건
        if (hasText(keyword)) {
            condition.and(user.name.containsIgnoreCase(keyword)
                    .or(user.email.containsIgnoreCase(keyword)));
        }
        return condition;
    }

    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }
}
