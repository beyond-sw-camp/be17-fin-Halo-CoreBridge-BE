package com.halo.core_bridge.api.personal.repository;

import com.halo.core_bridge.api.personal.model.entity.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {

    /**
     * resumeId로 분석 결과 조회
     */
    Optional<PersonalData> findByResumeId(Long resumeId);

    /**
     * userId와 resumeId로 분석 결과 조회 (추가)
     */
    Optional<PersonalData> findByUserIdAndResumeId(Long userId, Long resumeId);

    /**
     * 특정 사용자의 모든 Resume 분석 결과 조회 (최신순) (추가)
     */
    List<PersonalData> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 특정 사용자의 Resume 분석 결과 조회 (점수순) (추가)
     */
    List<PersonalData> findByUserIdOrderByTotalScoreDesc(Long userId);

    /**
     * 특정 점수 이상인 지원자 조회
     */
    List<PersonalData> findByTotalScoreGreaterThanEqualOrderByTotalScoreDesc(Double minScore);

    /**
     * 최신 분석 결과 조회 (상위 N개)
     */
    List<PersonalData> findTop10ByOrderByCreatedAtDesc();

    /**
     * 점수별로 정렬하여 조회
     */
    @Query("SELECT p FROM PersonalData p ORDER BY p.totalScore DESC, p.createdAt DESC")
    List<PersonalData> findAllOrderByScoreDesc();

    /**
     * 매칭된 지원자만 조회 (60점 이상)
     */
    @Query("SELECT p FROM PersonalData p WHERE p.totalScore >= 60 ORDER BY p.totalScore DESC")
    List<PersonalData> findMatchedCandidates();

    /**
     * 특정 사용자의 매칭된 Resume 조회 (60점 이상) (추가)
     */
    @Query("SELECT p FROM PersonalData p WHERE p.userId = :userId AND p.totalScore >= 60 ORDER BY p.totalScore DESC")
    List<PersonalData> findMatchedResumesByUserId(Long userId);
}
