package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long>, ApplicantQueryRepository {
    List<Resume> findByJobPostingIdAndProcessId(Long jobPostingId, Long processId);

    List<Resume> findByJobPostingId(Long jobPostingId);

    @Query("SELECT COUNT(r) FROM Resume r WHERE r.jobPosting.id = :jobPostingId")
    int countByJobPostingId(Long jobPostingId);

    @Query("SELECT COUNT(r) FROM Resume r WHERE r.process = :recruitProcess")
    int countByRecruitProcess(RecruitProcess recruitProcess);

    // 지원자의 프로필 지원 개수 조회용
    long countByUserId(Long userId);

    // 지원자의 마이페이지: Resume + JobPosting + Process fetch join
    @Query("""
    select r from Resume r
    join fetch r.jobPosting jp
    join fetch r.process p
    where r.user.id = :userId
    order by r.createdAt desc
""")
    List<Resume> findByUserIdWithPostingAndProcess(Long userId);
}