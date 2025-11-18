package com.halo.core_bridge.api.personal.controller;

import com.halo.core_bridge.api.personal.model.dto.N8nAnalysisDto;
import com.halo.core_bridge.api.personal.model.dto.PersonalDataDto;
import com.halo.core_bridge.api.personal.service.PersonalDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/personal-data")
@RequiredArgsConstructor
public class PersonalDataController {

    private final PersonalDataService personalDataService;

    /**
     * AI 분석 결과 저장 API (n8n에서 호출)
     * n8n에서 보내는 데이터 구조를 PersonalDataDto로 변환하여 저장
     */
    @PostMapping("/result")
    public ResponseEntity<?> saveAnalysisResult(
            @RequestBody N8nAnalysisDto.Request n8nRequest) {

        log.info("AI 분석 결과 수신 - userId: {}, resumeId: {}",
                n8nRequest.getUserId(), n8nRequest.getResumeId());
        log.debug("요청 데이터: {}", n8nRequest);

        try {
            // userId 유효성 검증
            if (n8nRequest.getUserId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "userId는 필수 값입니다."
                ));
            }

            // n8n 데이터 구조를 PersonalDataDto로 변환
            PersonalDataDto.Request request = convertToPersonalDataDto(n8nRequest);

            Long savedId = personalDataService.saveAnalysisResult(request);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "AI 분석 결과가 성공적으로 저장되었습니다.",
                    "savedId", savedId,
                    "userId", request.getUserId(),
                    "resumeId", request.getResumeId()
            ));

        } catch (Exception e) {
            log.error("AI 분석 결과 저장 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "저장 중 오류가 발생했습니다: " + e.getMessage()
            ));
        }
    }

    /**
     * n8n 데이터 구조를 PersonalDataDto로 변환
     */
    private PersonalDataDto.Request convertToPersonalDataDto(N8nAnalysisDto.Request n8nRequest) {

        // Recommend 변환
        List<PersonalDataDto.RecommendResult> recommendResults = null;
        if (n8nRequest.getRecommend() != null) {
            recommendResults = n8nRequest.getRecommend().stream()
                    .map(item -> PersonalDataDto.RecommendResult.builder()
                            .candidateId(item.getKey())
                            .score(item.getScore())
                            .build())
                    .collect(Collectors.toList());
        }

        // ScoreResult 변환
        PersonalDataDto.ScoreDetail scoreDetail = null;
        if (n8nRequest.getScoreResult() != null) {
            N8nAnalysisDto.ScoreResult sr = n8nRequest.getScoreResult();

            scoreDetail = PersonalDataDto.ScoreDetail.builder()
                    .cosineSimilarity(sr.getCosineSimilarity())
                    .skillRatio(sr.getSkillRatio())
                    .skillScore(sr.getSkillScore())
                    .simScore(sr.getSimScore())
                    .bonus(sr.getBonus())
                    .totalScore(sr.getTotal())
                    .build();
        }

        return PersonalDataDto.Request.builder()
                .userId(n8nRequest.getUserId())      // userId 매핑 ✨
                .resumeId(n8nRequest.getResumeId())
                .summary(n8nRequest.getSummary())
                .skills(n8nRequest.getSkills())
                .recommendResults(recommendResults)
                .scoreDetail(scoreDetail)
                .build();
    }

    /**
     * 저장된 AI 분석 결과 조회 (resumeId 기준)
     */
    @GetMapping("/result/{resumeId}")
    public ResponseEntity<?> getAnalysisResult(@PathVariable Long resumeId) {
        try {
            PersonalDataDto.Response result = personalDataService.getAnalysisResult(resumeId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("AI 분석 결과 조회 실패 - resumeId: {}", resumeId, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 특정 사용자의 특정 Resume 분석 결과 조회 (추가) ✨
     */
    @GetMapping("/result/user/{userId}/resume/{resumeId}")
    public ResponseEntity<?> getUserResumeAnalysis(
            @PathVariable Long userId,
            @PathVariable Long resumeId) {
        try {
            PersonalDataDto.Response result =
                    personalDataService.getUserResumeAnalysis(userId, resumeId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("AI 분석 결과 조회 실패 - userId: {}, resumeId: {}", userId, resumeId, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 특정 사용자의 Resume 변경 이력 조회 (추가) ✨
     * 방안 2를 사용하는 경우 필요한 API
     */
    @GetMapping("/history/user/{userId}/resume/{resumeId}")
    public ResponseEntity<?> getUserResumeHistory(
            @PathVariable Long userId,
            @PathVariable Long resumeId) {
        try {
            List<PersonalDataDto.Response> history =
                    personalDataService.getUserResumeHistory(userId, resumeId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            log.error("Resume 이력 조회 실패 - userId: {}, resumeId: {}", userId, resumeId, e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 매칭된 지원자 목록 조회 (60점 이상)
     */
    @GetMapping("/matched")
    public ResponseEntity<?> getMatchedCandidates() {
        try {
            List<PersonalDataDto.Response> results = personalDataService.getMatchedCandidates();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("매칭 결과 조회 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 전체 결과 점수순 조회
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllResults() {
        try {
            List<PersonalDataDto.Response> results = personalDataService.getAllResultsOrderByScore();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("전체 결과 조회 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 특정 점수 이상 지원자 조회
     */
    @GetMapping("/above-score")
    public ResponseEntity<?> getCandidatesAboveScore(@RequestParam Double minScore) {
        try {
            List<PersonalDataDto.Response> results = personalDataService.getCandidatesAboveScore(minScore);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("점수별 조회 실패 - minScore: {}", minScore, e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
