package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.dto.InterviewDto;
import com.halo.core_bridge.api.interview.model.entity.Interview;
import com.halo.core_bridge.api.interview.model.enums.InterviewStatus;
import com.halo.core_bridge.api.interview.repository.InterviewRepository;
import com.halo.core_bridge.api.jobposting.model.entity.RecruitProcess;
import com.halo.core_bridge.api.resume.model.entity.Resume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;

    @InjectMocks
    private InterviewService interviewService;

    @Test
    @DisplayName("면접 저장 성공")
    void save() {

        // given
        Interview interview = Interview.builder()
                .id(1L)
                .recruitProcess(
                        RecruitProcess.builder().id(1L).build()
                )
                .resume(
                        Resume.builder().id(1L).build()
                )
                .description("면접상세")
                .duration(60)
                .status(InterviewStatus.ONGOING)
                .startDateTime(LocalDateTime.now())
                .build();

        InterviewDto.Create interviewCreateDto = InterviewDto.Create.builder()
                .startDate(LocalDate.now())
                .startTime(LocalTime.now())
                .description("면접상세")
                .duration(60)
                .recruiterProcessId(1L)
                .resumeId(1L)
                .build();

        BDDMockito.given(interviewRepository.save(any(Interview.class))).willReturn(interview);

        // when
        Long savedId = interviewService.save(interviewCreateDto);

        // then
        assertNotNull(savedId);
        assertEquals(interview.getId(), savedId);
    }
}