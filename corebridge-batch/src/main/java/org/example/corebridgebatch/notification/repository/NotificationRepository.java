package org.example.corebridgebatch.notification.repository;

import org.example.corebridgebatch.notification.model.entity.Notification;
import org.example.corebridgebatch.notification.model.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * ✅ 재전송 대상 알림 조회 (배치용)
     * - 상태: UNSENT
     * - 재시도 횟수 제한: retryCount < 5
     * - timestamp 오름차순 정렬
     */
    @Query("SELECT n FROM Notification n " +
            "WHERE n.status IN :statuses " +
            "AND n.retryCount < 5 " +
            "ORDER BY n.timestamp ASC")
    Page<Notification> findForRetry(@Param("statuses") List<DeliveryStatus> statuses,
                                                                                          Pageable pageable);

    /**
     * 특정 유저의 미전송 알림 조회 (SSE 재연결 시 사용)
     * - 최근 24시간 이내의 UNSENT 알림만 조회
     */
    @Query("SELECT n FROM Notification n " +
            "WHERE n.userId = :userId " +
            "AND n.status = 'UNSENT' " +
            "AND n.timestamp > :since " +
            "ORDER BY n.timestamp ASC")
    List<Notification> findUnsentByUserId(
            @Param("userId") Long userId,
            @Param("since") Long since
    );

    @Query("SELECT n FROM Notification n WHERE (n.status = 'UNSENT' OR n.status = 'SENT') AND n.status <> 'EMAIL_SENT' AND n.status <> 'READ'")
    List<Notification> findEmailTargets();


    /**
     * 편의 메서드: 최근 24시간 미전송 알림 조회
     */
    default List<Notification> findUnsentByUserId(Long userId) {
        long oneDayAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000L);
        return findUnsentByUserId(userId, oneDayAgo);
    }

    /**
     * 특정 유저의 모든 알림 조회 (최신순)
     */
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.timestamp DESC")
    List<Notification> findByUserIdOrderByTimestampDesc(@Param("userId") Long userId);

    /**
     * 특정 유저의 읽지 않은 알림 수 조회
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.status <> 'READ'")
    long countUnreadByUserId(Long userId);


    List<Notification> findByStatus(DeliveryStatus status);

    /**
     * 오래된 SENT 알림 삭제 (30일 이상)
     * - 배치에서 주기적으로 호출하여 DB 정리
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.status = 'SENT' AND n.timestamp < :before")
    void deleteOldSentNotifications(@Param("before") Long before);

    /**
     * 전송 실패 횟수가 임계값을 초과한 알림 조회
     * - 재전송 포기 대상
     */
    @Query("SELECT n FROM Notification n WHERE n.status = 'UNSENT' AND n.retryCount >= :maxRetry")
    List<org.example.corebridgebatch.notification.model.entity.Notification> findFailedNotifications(@Param("maxRetry") int maxRetry);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.status = 'SENT' AND n.timestamp < :threshold")
    int deleteOldSentNotifications(long threshold);

}
