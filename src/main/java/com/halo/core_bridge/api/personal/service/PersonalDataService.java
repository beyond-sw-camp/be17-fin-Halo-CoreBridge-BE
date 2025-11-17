package com.halo.core_bridge.api.personal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.core_bridge.api.personal.model.dto.PersonalDataDto;
import com.halo.core_bridge.api.personal.model.entity.PersonalData;
import com.halo.core_bridge.api.personal.model.entity.PersonalDataHistory;
import com.halo.core_bridge.api.personal.repository.PersonalDataHistoryRepository;
import com.halo.core_bridge.api.personal.repository.PersonalDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalDataService {

    private final PersonalDataRepository personalDataRepository;
    private final PersonalDataHistoryRepository personalDataHistoryRepository;
    private final ObjectMapper objectMapper;

    /**
     * AI 분석 결과 저장 - 방안 2: History에 모든 이력 기록, PersonalData는 최신 상태만 유지
     */
    @Transactional
    public Long saveAnalysisResult(PersonalDataDto.Request request) {
        try {
            log.info("AI 분석 결과 저장 시작 - userId: {}, resumeId: {}",
                    request.getUserId(), request.getResumeId());

            // 기존 데이터 조회
            PersonalData existing = personalDataRepository
                    .findByUserIdAndResumeId(request.getUserId(), request.getResumeId())
                    .orElse(null);

            // JSON 변환
            String skillsJson = objectMapper.writeValueAsString(request.getSkills());
            String recommendJson = request.getRecommendResults() != null ?
                    objectMapper.writeValueAsString(request.getRecommendResults()) : null;

            PersonalDataDto.ScoreDetail sd = request.getScoreDetail();

            // ✨ 항상 History에 현재 요청 기록 (신규든 업데이트든 모두 기록)
            PersonalDataHistory history = PersonalDataHistory.builder()
                    .userId(request.getUserId())
                    .resumeId(request.getResumeId())
                    .summary(request.getSummary())
                    .skills(skillsJson)
                    .recommendResults(recommendJson)
                    .cosineSimilarity(sd != null ? sd.getCosineSimilarity() : null)
                    .skillRatio(sd != null ? sd.getSkillRatio() : null)
                    .skillScore(sd != null ? sd.getSkillScore() : null)
                    .simScore(sd != null ? sd.getSimScore() : null)
                    .bonus(sd != null ? sd.getBonus() : null)
                    .totalScore(sd != null ? sd.getTotalScore() : null)
                    .build();

            PersonalDataHistory savedHistory = personalDataHistoryRepository.save(history);
            log.info("History 기록 완료 - historyId: {}", savedHistory.getId());

            // PersonalData는 최신 상태만 유지 (없으면 새로 생성, 있으면 업데이트)
            PersonalData updated = existing != null ? existing :
                    PersonalData.builder()
                            .userId(request.getUserId())
                            .resumeId(request.getResumeId())
                            .build();

            updated.updateAnalysisResult(
                    request.getSummary(),
                    skillsJson,
                    recommendJson,
                    sd != null ? sd.getCosineSimilarity() : null,
                    sd != null ? sd.getSkillRatio() : null,
                    sd != null ? sd.getSkillScore() : null,
                    sd != null ? sd.getSimScore() : null,
                    sd != null ? sd.getBonus() : null,
                    sd != null ? sd.getTotalScore() : null
            );

            PersonalData saved = personalDataRepository.save(updated);

            log.info("AI 분석 결과 저장 완료 - id: {}, userId: {}, resumeId: {}, 기존데이터: {}",
                    saved.getId(), saved.getUserId(), saved.getResumeId(), existing != null);

            return saved.getId();

        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패 - userId: {}, resumeId: {}",
                    request.getUserId(), request.getResumeId(), e);
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }
    }

    /**
     * 분석 결과 조회 (resumeId 기준)
     */
    @Transactional(readOnly = true)
    public PersonalDataDto.Response getAnalysisResult(Long resumeId) {
        PersonalData data = personalDataRepository.findByResumeId(resumeId)
                .orElseThrow(() -> new RuntimeException("분석 결과를 찾을 수 없습니다. resumeId: " + resumeId));

        return convertToResponse(data);
    }

    /**
     * 특정 사용자의 Resume 분석 결과 조회 (최신 상태)
     */
    @Transactional(readOnly = true)
    public PersonalDataDto.Response getUserResumeAnalysis(Long userId, Long resumeId) {
        PersonalData data = personalDataRepository.findByUserIdAndResumeId(userId, resumeId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("분석 결과를 찾을 수 없습니다. userId: %d, resumeId: %d", userId, resumeId)));

        return convertToResponse(data);
    }

    /**
     * 특정 사용자의 Resume 변경 이력 조회
     */
    @Transactional(readOnly = true)
    public List<PersonalDataDto.Response> getUserResumeHistory(Long userId, Long resumeId) {
        return personalDataHistoryRepository
                .findByUserIdAndResumeIdOrderByCreatedAtDesc(userId, resumeId)
                .stream()
                .map(this::convertHistoryToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자의 모든 Resume 변경 이력 조회
     */
    @Transactional(readOnly = true)
    public List<PersonalDataDto.Response> getAllUserResumeHistory(Long userId) {
        return personalDataHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertHistoryToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 매칭된 지원자 목록 조회 (60점 이상)
     */
    @Transactional(readOnly = true)
    public List<PersonalDataDto.Response> getMatchedCandidates() {
        return personalDataRepository.findMatchedCandidates()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 전체 결과 점수순 조회
     */
    @Transactional(readOnly = true)
    public List<PersonalDataDto.Response> getAllResultsOrderByScore() {
        return personalDataRepository.findAllOrderByScoreDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 점수 이상 지원자 조회
     */
    @Transactional(readOnly = true)
    public List<PersonalDataDto.Response> getCandidatesAboveScore(Double minScore) {
        return personalDataRepository.findByTotalScoreGreaterThanEqualOrderByTotalScoreDesc(minScore)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity를 Response DTO로 변환
     */
    private PersonalDataDto.Response convertToResponse(PersonalData data) {
        try {
            // JSON 역직렬화
            List<String> skills = data.getSkills() != null ?
                    objectMapper.readValue(data.getSkills(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)) : null;

            List<PersonalDataDto.RecommendResult> recommendResults = data.getRecommendResults() != null ?
                    objectMapper.readValue(data.getRecommendResults(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, PersonalDataDto.RecommendResult.class)) : null;

            // ScoreDetail 구성
            PersonalDataDto.ScoreDetail scoreDetail = PersonalDataDto.ScoreDetail.builder()
                    .cosineSimilarity(data.getCosineSimilarity())
                    .skillRatio(data.getSkillRatio())
                    .skillScore(data.getSkillScore())
                    .simScore(data.getSimScore())
                    .bonus(data.getBonus())
                    .totalScore(data.getTotalScore())
                    .build();

            return PersonalDataDto.Response.builder()
                    .id(data.getId())
                    .userId(data.getUserId())
                    .resumeId(data.getResumeId())
                    .summary(data.getSummary())
                    .skills(skills)
                    .recommendResults(recommendResults)
                    .scoreDetail(scoreDetail)
                    .grade(data.getGrade())
                    .isMatched(data.isMatched())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("JSON 역직렬화 실패 - ID: {}", data.getId(), e);
            throw new RuntimeException("데이터 변환 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * History Entity를 Response DTO로 변환
     */
    private PersonalDataDto.Response convertHistoryToResponse(PersonalDataHistory history) {
        try {
            // JSON 역직렬화
            List<String> skills = history.getSkills() != null ?
                    objectMapper.readValue(history.getSkills(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)) : null;

            List<PersonalDataDto.RecommendResult> recommendResults = history.getRecommendResults() != null ?
                    objectMapper.readValue(history.getRecommendResults(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, PersonalDataDto.RecommendResult.class)) : null;

            // ScoreDetail 구성
            PersonalDataDto.ScoreDetail scoreDetail = PersonalDataDto.ScoreDetail.builder()
                    .cosineSimilarity(history.getCosineSimilarity())
                    .skillRatio(history.getSkillRatio())
                    .skillScore(history.getSkillScore())
                    .simScore(history.getSimScore())
                    .bonus(history.getBonus())
                    .totalScore(history.getTotalScore())
                    .build();

            // History는 등급 계산
            String grade = calculateGrade(history.getTotalScore());
            boolean isMatched = history.getTotalScore() != null && history.getTotalScore() >= 60.0;

            return PersonalDataDto.Response.builder()
                    .id(history.getId())
                    .userId(history.getUserId())
                    .resumeId(history.getResumeId())
                    .summary(history.getSummary())
                    .skills(skills)
                    .recommendResults(recommendResults)
                    .scoreDetail(scoreDetail)
                    .grade(grade)
                    .isMatched(isMatched)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("JSON 역직렬화 실패 - History ID: {}", history.getId(), e);
            throw new RuntimeException("데이터 변환 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 점수 기반 등급 계산
     */
    private String calculateGrade(Double totalScore) {
        if (totalScore == null) return "N/A";
        if (totalScore >= 80) return "S";
        if (totalScore >= 60) return "A";
        if (totalScore >= 40) return "B";
        if (totalScore >= 20) return "C";
        return "D";
    }
}
