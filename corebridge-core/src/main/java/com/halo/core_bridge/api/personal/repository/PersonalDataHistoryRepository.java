package com.halo.core_bridge.api.personal.repository;

import com.halo.core_bridge.api.personal.model.entity.PersonalDataHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalDataHistoryRepository extends JpaRepository<PersonalDataHistory, Long> {

    /**
     * 특정 사용자의 모든 이력 조회 (최신순) (추가)
     */
    List<PersonalDataHistory> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 특정 Resume의 모든 이력 조회 (최신순) (추가)
     */
    List<PersonalDataHistory> findByResumeIdOrderByCreatedAtDesc(Long resumeId);

    /**
     * 특정 사용자의 특정 Resume 이력 조회 (최신순) (추가)
     */
    List<PersonalDataHistory> findByUserIdAndResumeIdOrderByCreatedAtDesc(Long userId, Long resumeId);

    /**
     * 특정 사용자의 최근 N개 이력 조회 (추가)
     */
    List<PersonalDataHistory> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
