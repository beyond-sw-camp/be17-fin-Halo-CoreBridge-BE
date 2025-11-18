package com.halo.core_bridge.api.ai.test;

import com.halo.core_bridge.api.ai.dto.AiDtos;
import com.halo.core_bridge.api.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI API 전체 호출 시간 측정 테스트
 * 
 * WebClient 순차 호출 방식의 성능을 측정하여
 * n8n 워크플로우 전환의 정당성을 입증
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiPerformanceTest {

    private final AiService aiService;

    /**
     * 전체 AI 분석 프로세스 실행 및 시간 측정
     * 
     * @param resumeText 이력서 내용
     * @param jdText 채용공고 내용
     * @param candidateId 지원자 ID
     * @return 측정 결과
     */
    public PerformanceResult measureFullAnalysis(String resumeText, String jdText, String candidateId) {
        log.info("========================================");
        log.info("AI 전체 분석 성능 측정 시작");
        log.info("========================================");

        PerformanceResult result = new PerformanceResult();
        long totalStart = System.currentTimeMillis();

        try {
            // 1. Summary (요약)
            long summaryStart = System.currentTimeMillis();
            String summary = aiService.summarize(resumeText);
            long summaryEnd = System.currentTimeMillis();
            long summaryTime = summaryEnd - summaryStart;
            result.setSummaryTime(summaryTime);
            result.setSummary(summary);
            log.info("✅ [1/5] Summary 완료: {}ms", summaryTime);

            // 2. Skills (기술 추출)
            long skillsStart = System.currentTimeMillis();
            List<String> skills = aiService.extractSkills(resumeText);
            long skillsEnd = System.currentTimeMillis();
            long skillsTime = skillsEnd - skillsStart;
            result.setSkillsTime(skillsTime);
            result.setSkills(skills);
            log.info("✅ [2/5] Skills 완료: {}ms, 추출된 기술: {}", skillsTime, skills);

            // 3. Save Resume (벡터 저장)
            long saveStart = System.currentTimeMillis();
            aiService.saveResume(candidateId, resumeText);
            long saveEnd = System.currentTimeMillis();
            long saveTime = saveEnd - saveStart;
            result.setSaveTime(saveTime);
            log.info("✅ [3/5] Save Resume 완료: {}ms", saveTime);

            // 4. Match JD (매칭)
            long matchStart = System.currentTimeMillis();
            AiDtos.MatchRes matchRes = aiService.matchJd(jdText, skills, 5);
            long matchEnd = System.currentTimeMillis();
            long matchTime = matchEnd - matchStart;
            result.setMatchTime(matchTime);
            result.setMatchResult(matchRes);
            log.info("✅ [4/5] Match JD 완료: {}ms, 매칭 결과: {}건", matchTime, 
                    matchRes.matches() != null ? matchRes.matches().size() : 0);

            // 5. Score (점수 계산)
            long scoreStart = System.currentTimeMillis();
            AiDtos.ScoreRes scoreRes = aiService.score(jdText, candidateId, skills);
            long scoreEnd = System.currentTimeMillis();
            long scoreTime = scoreEnd - scoreStart;
            result.setScoreTime(scoreTime);
            result.setScoreResult(scoreRes);
            log.info("✅ [5/5] Score 완료: {}ms, 총점: {}", scoreTime, 
                    scoreRes.score_detail != null ? scoreRes.score_detail.total : null);

            long totalEnd = System.currentTimeMillis();
            long totalTime = totalEnd - totalStart;
            result.setTotalTime(totalTime);
            result.setSuccess(true);

            log.info("========================================");
            log.info("📊 AI 전체 분석 완료");
            log.info("========================================");
            log.info("Summary   : {}ms", summaryTime);
            log.info("Skills    : {}ms", skillsTime);
            log.info("Save      : {}ms", saveTime);
            log.info("Match     : {}ms", matchTime);
            log.info("Score     : {}ms", scoreTime);
            log.info("----------------------------------------");
            log.info("🕐 총 소요 시간: {}ms ({}초)", totalTime, String.format("%.2f", totalTime / 1000.0));
            log.info("========================================");

            return result;

        } catch (Exception e) {
            long totalEnd = System.currentTimeMillis();
            long totalTime = totalEnd - totalStart;
            result.setTotalTime(totalTime);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());

            log.error("❌ AI 분석 중 오류 발생: {}ms 소요 후 실패", totalTime, e);
            return result;
        }
    }

    /**
     * 여러 번 실행하여 평균 시간 측정
     * 
     * @param resumeText 이력서 내용
     * @param jdText 채용공고 내용
     * @param candidateId 지원자 ID
     * @param iterations 반복 횟수
     * @return 평균 측정 결과
     */
    public AveragePerformanceResult measureAveragePerformance(
            String resumeText, String jdText, String candidateId, int iterations) {
        
        log.info("========================================");
        log.info("AI 평균 성능 측정 시작 ({}회 반복)", iterations);
        log.info("========================================");

        long totalSummaryTime = 0;
        long totalSkillsTime = 0;
        long totalSaveTime = 0;
        long totalMatchTime = 0;
        long totalScoreTime = 0;
        long totalTotalTime = 0;
        int successCount = 0;

        for (int i = 1; i <= iterations; i++) {
            log.info("\n[{}/{}] 실행 중...", i, iterations);
            
            PerformanceResult result = measureFullAnalysis(resumeText, jdText, candidateId + "_" + i);
            
            if (result.isSuccess()) {
                totalSummaryTime += result.getSummaryTime();
                totalSkillsTime += result.getSkillsTime();
                totalSaveTime += result.getSaveTime();
                totalMatchTime += result.getMatchTime();
                totalScoreTime += result.getScoreTime();
                totalTotalTime += result.getTotalTime();
                successCount++;
            }

            // 다음 실행 전 1초 대기 (API 서버 부하 방지)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        AveragePerformanceResult avgResult = new AveragePerformanceResult();
        if (successCount > 0) {
            avgResult.setAvgSummaryTime(totalSummaryTime / successCount);
            avgResult.setAvgSkillsTime(totalSkillsTime / successCount);
            avgResult.setAvgSaveTime(totalSaveTime / successCount);
            avgResult.setAvgMatchTime(totalMatchTime / successCount);
            avgResult.setAvgScoreTime(totalScoreTime / successCount);
            avgResult.setAvgTotalTime(totalTotalTime / successCount);
        }
        avgResult.setIterations(iterations);
        avgResult.setSuccessCount(successCount);

        log.info("\n========================================");
        log.info("📊 평균 성능 측정 결과 ({}/{}회 성공)", successCount, iterations);
        log.info("========================================");
        log.info("Summary   : {}ms", avgResult.getAvgSummaryTime());
        log.info("Skills    : {}ms", avgResult.getAvgSkillsTime());
        log.info("Save      : {}ms", avgResult.getAvgSaveTime());
        log.info("Match     : {}ms", avgResult.getAvgMatchTime());
        log.info("Score     : {}ms", avgResult.getAvgScoreTime());
        log.info("----------------------------------------");
        log.info("🕐 평균 총 소요 시간: {}ms ({}초)", 
                avgResult.getAvgTotalTime(), 
                String.format("%.2f", avgResult.getAvgTotalTime() / 1000.0));
        log.info("========================================");

        return avgResult;
    }

    /**
     * 단일 측정 결과
     */
    public static class PerformanceResult {
        private long summaryTime;
        private long skillsTime;
        private long saveTime;
        private long matchTime;
        private long scoreTime;
        private long totalTime;
        private boolean success;
        private String errorMessage;

        // 결과 데이터
        private String summary;
        private List<String> skills;
        private AiDtos.MatchRes matchResult;
        private AiDtos.ScoreRes scoreResult;

        // Getters and Setters
        public long getSummaryTime() { return summaryTime; }
        public void setSummaryTime(long summaryTime) { this.summaryTime = summaryTime; }

        public long getSkillsTime() { return skillsTime; }
        public void setSkillsTime(long skillsTime) { this.skillsTime = skillsTime; }

        public long getSaveTime() { return saveTime; }
        public void setSaveTime(long saveTime) { this.saveTime = saveTime; }

        public long getMatchTime() { return matchTime; }
        public void setMatchTime(long matchTime) { this.matchTime = matchTime; }

        public long getScoreTime() { return scoreTime; }
        public void setScoreTime(long scoreTime) { this.scoreTime = scoreTime; }

        public long getTotalTime() { return totalTime; }
        public void setTotalTime(long totalTime) { this.totalTime = totalTime; }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }

        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }

        public AiDtos.MatchRes getMatchResult() { return matchResult; }
        public void setMatchResult(AiDtos.MatchRes matchResult) { this.matchResult = matchResult; }

        public AiDtos.ScoreRes getScoreResult() { return scoreResult; }
        public void setScoreResult(AiDtos.ScoreRes scoreResult) { this.scoreResult = scoreResult; }
    }

    /**
     * 평균 측정 결과
     */
    public static class AveragePerformanceResult {
        private long avgSummaryTime;
        private long avgSkillsTime;
        private long avgSaveTime;
        private long avgMatchTime;
        private long avgScoreTime;
        private long avgTotalTime;
        private int iterations;
        private int successCount;

        // Getters and Setters
        public long getAvgSummaryTime() { return avgSummaryTime; }
        public void setAvgSummaryTime(long avgSummaryTime) { this.avgSummaryTime = avgSummaryTime; }

        public long getAvgSkillsTime() { return avgSkillsTime; }
        public void setAvgSkillsTime(long avgSkillsTime) { this.avgSkillsTime = avgSkillsTime; }

        public long getAvgSaveTime() { return avgSaveTime; }
        public void setAvgSaveTime(long avgSaveTime) { this.avgSaveTime = avgSaveTime; }

        public long getAvgMatchTime() { return avgMatchTime; }
        public void setAvgMatchTime(long avgMatchTime) { this.avgMatchTime = avgMatchTime; }

        public long getAvgScoreTime() { return avgScoreTime; }
        public void setAvgScoreTime(long avgScoreTime) { this.avgScoreTime = avgScoreTime; }

        public long getAvgTotalTime() { return avgTotalTime; }
        public void setAvgTotalTime(long avgTotalTime) { this.avgTotalTime = avgTotalTime; }

        public int getIterations() { return iterations; }
        public void setIterations(int iterations) { this.iterations = iterations; }

        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
    }
}
