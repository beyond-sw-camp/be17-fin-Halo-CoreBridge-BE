package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.dto.TeckStackDto.TeckStackResponse;
import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.jobposting.model.entity.TechStack;
import com.halo.core_bridge.api.jobposting.repository.JobPostingSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingSkillService {
    private final JobPostingSkillRepository jobPostingSkillRepository;

    public void saveAll(List<String> skillNames, Long jobPostingId) {

        List<JobPostingSkill> jobPostingSkills = skillNames.stream()
                .map(name -> JobPostingSkill.builder()
                        .name(TechStack.valueOf(name))
                        .jobPosting(JobPosting.builder().id(jobPostingId).build())
                        .build())
                .toList();

        jobPostingSkillRepository.saveAll(jobPostingSkills);
    }

    // 현재 보유한 모든 기술스택 응답
    public List<TeckStackResponse> getAllTechStacks() {
        return Arrays.stream(TechStack.values()).map(ts -> new TeckStackResponse(ts.name(), ts.getLabel()))
                .toList();
    }
}
