package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.entity.JobPosting;
import com.halo.core_bridge.api.jobposting.model.entity.JobPostingSkill;
import com.halo.core_bridge.api.jobposting.repository.JobPostingSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobPostingSkillService {
    private final JobPostingSkillRepository jobPostingSkillRepository;

    public void saveAll(List<String> skillNames, Long jobPostingId) {

        List<JobPostingSkill> jobPostingSkills = skillNames.stream()
                .map(name -> JobPostingSkill.builder()
                        .name(name)
                        .jobPosting(JobPosting.builder().id(jobPostingId).build())
                        .build())
                .toList();

        jobPostingSkillRepository.saveAll(jobPostingSkills);
    }
}
