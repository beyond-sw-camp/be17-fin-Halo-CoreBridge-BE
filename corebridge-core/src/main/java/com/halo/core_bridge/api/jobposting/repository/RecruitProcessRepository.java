package com.halo.core_bridge.api.jobposting.repository;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitProcessRepository extends JpaRepository<RecruitProcess, Long> {

    List<RecruitProcess> findByJobPosting_IdOrderByOrderIdxAsc(Long jobPostingId);

    List<RecruitProcess> findByJobPosting(JobPosting jobPosting);

    void deleteAllByJobPosting(JobPosting jobPosting);

    // 지원자가 지원한 채용공고 전체조회
    @Query("""
        select rp from RecruitProcess rp
        where rp.jobPosting.id in :jobPostingIds
        order by rp.jobPosting.id asc, rp.orderIdx asc
        """)
    List<RecruitProcess> findAllByJobPostingIds(@Param("jobPostingIds") List<Long> jobPostingIds);

    /**
     * <code>JobPostingId</code>의 채용 프로세스들 중에서 <br>
     * <code>orderIdx</code>가 </code><code>fromIdx</code>부터 <code>toIdx</code> 범위의 <code>orderIdx</code>값들을 1씩 증가 시킨다.
     * @param jobPostingId 채용프로세스 목록을 찾기위한 채용 공고 ID
     * @param fromIdx 기존 정렬 순서 Idx
     * @param toIdx 변경할 정렬 순서 Idx
     */
    @Modifying
    @Query("""
            update RecruitProcess rp
            set rp.orderIdx = rp.orderIdx + 1
            where rp.jobPosting.id = :jobPostingId
            and rp.orderIdx
            between :toIdx and :fromIdx - 1
    """)
    void incrementOrderIdxes(@Param("jobPostingId") Long jobPostingId, @Param("fromIdx") int fromIdx, @Param("toIdx") int toIdx);

    /**
     * <code>JobPostingId</code>의 채용 프로세스들 중에서 <br>
     * <code>orderIdx</code>가 </code><code>fromIdx</code>부터 <code>toIdx</code> 범위의 <code>orderIdx</code>값들을 1씩 감소 시킨다.
     * @param jobPostingId 채용프로세스 목록을 찾기위한 채용 공고 ID
     * @param fromIdx 기존 정렬 순서 Idx
     * @param toIdx 변경할 정렬 순서 Idx
     */
    @Modifying
    @Query("""
            update RecruitProcess rp
            set rp.orderIdx = rp.orderIdx + -1
            where rp.jobPosting.id = :jobPostingId
            and rp.orderIdx
            between :fromIdx + 1 and :toIdx
    """)
    void decrementOrderIdxes(@Param("jobPostingId") Long jobPostingId, @Param("fromIdx") int fromIdx, @Param("toIdx") int toIdx);

    /**
     * 채용 프로세스의 정렬 순서인 <code>orderIdx</code>를 <code>toIdx</code>로 변경한다.
     * @param processId 정렬순서 <code>orderIdx</code>를 수정할 채용 프로세스 Id
     * @param toIdx 변경할 정렬 순서
     */
    @Modifying
    @Query("""
            update RecruitProcess rp
            set rp.orderIdx = :toIdx
            where rp.id = :processId
    """)
    void updateOrderIdx(@Param("processId") Long processId, @Param("toIdx") int toIdx);

    /**
     * 특정 채용 공고의 채용 프로세스 중에서 orderIdx의 최대값을 조회한다.
     * @param jobPostingId 채용 공고 <code>id</code>
     * @return <code>Integer</code> - <code>orderIdx의</code> 최대값을 반환한다. 없다면 <code>null</code>을 반환한다.
     */
    @Query("select max(r.orderIdx) from RecruitProcess r where r.jobPosting.id = :jobPostingId")
    Integer findMaxOrderByJobPostingId(@Param("jobPostingId") Long jobPostingId);

    /**
     * 마지막 채용 프로세스의 <code>orderIdx</code>를 1 증가 시킨다.
     * @param jobPostingId 채용 공고 <code>id</code>
     * @param lastOrderIdx 마지막 프로세스의 <code>orderIdx</code>
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update RecruitProcess r set r.orderIdx = r.orderIdx + 1 where r.jobPosting.id = :jobPostingId and r.orderIdx = :lastOrderIdx")
    void shiftLastOrderIdx(@Param("jobPostingId") Long jobPostingId, @Param("lastOrderIdx") Integer lastOrderIdx);
}
