package com.halo.core_bridge.api.myPage.service;

import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessRepository;
import com.halo.core_bridge.api.myPage.model.MypageDto;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import com.halo.core_bridge.api.resume.repository.ResumeRepository;
import com.halo.core_bridge.api.users.model.entity.User;
import com.halo.core_bridge.api.users.repository.UserRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final ResumeRepository resumeRepository;
    private final RecruitProcessRepository recruitProcessRepository;
    private final UserRepository userRepository;

    public MypageDto.MyPageResponse getMyPage(Long UserId) {

        // 해당 유저의 지원 리스트 + 현재 프로세스 + 채용공고
        List<Resume> resumes = resumeRepository.findByUserIdWithPostingAndProcess(UserId);

        // 지원한 채용공고 id 리스트 추출
        List<Long> jobPostingIds = resumes.stream().map(r -> r.getJobPosting().getId()).distinct().toList();

        // 유저가 지원한 모든 공고별 채용프로세스 추출
        List<RecruitProcess> processes = recruitProcessRepository.findAllByJobPostingIds(jobPostingIds);

        // 공고별 프로세스의 이름만 추출 Map(jobPostingId -> List<processName>)으로 변경
        Map<Long, List<String>> processMap = new HashMap<>();
        for (RecruitProcess process : processes) {
            Long jobPostingId = process.getJobPosting().getId();
            processMap.computeIfAbsent(jobPostingId, k -> new ArrayList<>()).add(process.getName());
        }

        // AppliedJobResponse Dto 생성
        List<MypageDto.AppliedJobResponse> applicationDtos = resumes.stream()
                .map(resume -> {

                    Long postingId = resume.getJobPosting().getId();

                    return MypageDto.AppliedJobResponse.builder()
                            .jobPostingId(postingId)
                            .jobTitle(resume.getJobPosting().getTitle())
                            .departmentName(resume.getJobPosting().getDepartment().getName())
                            .appliedDate(resume.getCreatedAt().toLocalDate().toString())
                            .currentStage(resume.getProcess().getName())  // 현재 단계는 Resume.process
                            .process(processMap.get(postingId))           // 전체 단계 리스트
                            .build();

                })
                .toList();

        // ProfileResponse 생성
        User user = userRepository.findById(UserId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_USER));

        MypageDto.ProfileResponse profileDto = MypageDto.ProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .appliedCount(resumes.size())
                .build();

        // 최종 응답
        return MypageDto.MyPageResponse.builder()
                .profile(profileDto)
                .applications(applicationDtos)
                .build();
    }


}
