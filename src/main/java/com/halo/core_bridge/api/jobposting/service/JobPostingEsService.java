package com.halo.core_bridge.api.jobposting.service;

import com.halo.core_bridge.api.jobposting.model.document.JobPostingDoc;
import com.halo.core_bridge.api.jobposting.model.document.RecruitProcessDoc;
import com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto;
import com.halo.core_bridge.api.jobposting.model.entity.CareerType;
import com.halo.core_bridge.api.jobposting.model.entity.EmploymentType;
import com.halo.core_bridge.api.jobposting.repository.JobPostingEsRepository;
import com.halo.core_bridge.api.jobposting.repository.RecruitProcessEsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.halo.core_bridge.api.jobposting.model.dto.JobPostingDto.*;

@Service
@RequiredArgsConstructor
public class JobPostingEsService {

    private final JobPostingEsRepository jobPostingESRepository;
    private final RecruitProcessEsRepository recruitProcessESRepository;

    public JobPostingListDto searchJobPostings(JobPostingDto.SearchQuery searchQuery) {

        PageRequest pageable = PageRequest.of(searchQuery.getPage(), 10, Sort.by("id").descending());


        Page<JobPostingDoc> postings = jobPostingESRepository.findAllByTitle(searchQuery.getKeyword(), pageable);

        List<JobPostingListResponseDto> resultList = postings.stream()
                .map(posting -> {
                    List<RecruitProcessDoc> processes = recruitProcessESRepository.findByJobPostingId(posting.getId());

                    var processSummaries = processes.stream()
                            .map(proc -> new JobPostingListResponseDto.ProcessSummary(
                                    proc.getStageName(),
                                    proc.getCount(),
                                    proc.getOrderIndex()
                            ))
                            .collect(Collectors.toList());

                    return JobPostingListResponseDto.builder()
                            .id(posting.getId())
                            .title(posting.getTitle())
                            .departmentName(posting.getDepartmentName())
                            .employmentType(EmploymentType.valueOf(posting.getEmploymentType()))
                            .careerType(CareerType.valueOf(posting.getCareerType()))
                            .summaryText(posting.getSummaryText())
                            .status(posting.getStatus())
                            .hireEndDate(posting.getHireEndDate().toLocalDate())
                            .dday(posting.getDday())
                            .applicantCount(posting.getApplicantCount())
                            .progressPercent(posting.getProgressPercent())
                            .processSummaries(processSummaries)
                            .build();
                })
                .toList();

        return JobPostingListDto.from(resultList, postings.getNumber(), postings.getTotalPages(), postings.getTotalElements());
    }
}
