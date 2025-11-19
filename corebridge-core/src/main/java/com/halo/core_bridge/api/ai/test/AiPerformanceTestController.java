package com.halo.core_bridge.api.ai.test;

import com.halo.core_bridge.api.ai.test.AiPerformanceTest.AveragePerformanceResult;
import com.halo.core_bridge.api.ai.test.AiPerformanceTest.PerformanceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AI API 성능 측정 테스트 컨트롤러
 * 
 * 사용법:
 * 1. 단일 측정: GET /api/ai/test/performance
 * 2. 평균 측정: GET /api/ai/test/performance/average?iterations=5
 */
@Slf4j
@RestController
@RequestMapping("/api/ai/test")
@RequiredArgsConstructor
public class AiPerformanceTestController {

    private final AiPerformanceTest performanceTest;

    /**
     * 단일 성능 측정 테스트
     * 
     * GET /api/ai/test/performance
     * 
     * @return 측정 결과
     */
    @GetMapping("/performance")
    public ResponseEntity<PerformanceResult> testPerformance() {
        String resumeText = """
                저는 문제를 구조적으로 분석하고, 안정적인 서비스를 설계하는 백엔드 개발자입니다. 
                대학에서 IoT AI 융합공학을 전공하며 데이터 흐름과 시스템 구조에 대한 이해를 쌓았고, 
                이후 Java와 Spring Boot 기반의 프로젝트를 통해 실무형 개발 역량을 키웠습니다.
                
                대표적으로 실시간 채용 관리 시스템 CoreBridge를 설계하며 Spring Boot + JPA + MariaDB로 
                주요 API를 구현하고, Redis SSE를 이용해 실시간 알림 기능을 개발했습니다. 
                또한 Docker, Kubernetes, Jenkins를 활용해 CI/CD 환경을 구축하고, 
                Blue/Green 및 Canary 배포를 적용하여 무중단 배포를 실현했습니다.
                
                저는 새로운 기술을 학습하는 것을 즐기며, Kafka, AWS, Vue.js, FastAPI 등 
                다양한 기술 스택을 프로젝트에 실험적으로 적용해보았습니다. 
                협업에서는 GitHub 기반 브랜치 전략과 코드 리뷰를 적극 도입해 팀 생산성을 높였습니다.
                
                앞으로는 대규모 서비스 환경에서 클라우드 기반 백엔드 아키텍처를 고도화하며, 
                효율적이고 안정적인 서비스를 제공하는 엔지니어로 성장하고 싶습니다.
                """;

        String jdText = """
                백엔드 개발자를 모집합니다.
                
                필수 기술:
                - Java, Spring Boot
                - Redis, AWS
                - RESTful API 설계
                
                우대 사항:
                - Kafka 경험
                - Kubernetes 운영 경험
                - CI/CD 구축 경험
                """;

        String candidateId = "test-candidate-001";

        PerformanceResult result = performanceTest.measureFullAnalysis(
                resumeText, jdText, candidateId);

        return ResponseEntity.ok(result);
    }

    /**
     * 평균 성능 측정 테스트 (여러 번 반복)
     * 
     * GET /api/ai/test/performance/average?iterations=5
     * 
     * @param iterations 반복 횟수 (기본값: 3)
     * @return 평균 측정 결과
     */
    @GetMapping("/performance/average")
    public ResponseEntity<AveragePerformanceResult> testAveragePerformance(
            @RequestParam(defaultValue = "3") int iterations) {

        if (iterations < 1 || iterations > 10) {
            throw new IllegalArgumentException("iterations는 1~10 사이여야 합니다.");
        }

        String resumeText = """
                저는 문제를 구조적으로 분석하고, 안정적인 서비스를 설계하는 백엔드 개발자입니다. 
                대학에서 IoT AI 융합공학을 전공하며 데이터 흐름과 시스템 구조에 대한 이해를 쌓았고, 
                이후 Java와 Spring Boot 기반의 프로젝트를 통해 실무형 개발 역량을 키웠습니다.
                
                대표적으로 실시간 채용 관리 시스템 CoreBridge를 설계하며 Spring Boot + JPA + MariaDB로 
                주요 API를 구현하고, Redis SSE를 이용해 실시간 알림 기능을 개발했습니다. 
                또한 Docker, Kubernetes, Jenkins를 활용해 CI/CD 환경을 구축하고, 
                Blue/Green 및 Canary 배포를 적용하여 무중단 배포를 실현했습니다.
                
                저는 새로운 기술을 학습하는 것을 즐기며, Kafka, AWS, Vue.js, FastAPI 등 
                다양한 기술 스택을 프로젝트에 실험적으로 적용해보았습니다. 
                협업에서는 GitHub 기반 브랜치 전략과 코드 리뷰를 적극 도입해 팀 생산성을 높였습니다.
                
                앞으로는 대규모 서비스 환경에서 클라우드 기반 백엔드 아키텍처를 고도화하며, 
                효율적이고 안정적인 서비스를 제공하는 엔지니어로 성장하고 싶습니다.
                """;

        String jdText = """
                백엔드 개발자를 모집합니다.
                
                필수 기술:
                - Java, Spring Boot
                - Redis, AWS
                - RESTful API 설계
                
                우대 사항:
                - Kafka 경험
                - Kubernetes 운영 경험
                - CI/CD 구축 경험
                """;

        String candidateId = "test-candidate-avg";

        AveragePerformanceResult result = performanceTest.measureAveragePerformance(
                resumeText, jdText, candidateId, iterations);

        return ResponseEntity.ok(result);
    }

    /**
     * 커스텀 데이터로 성능 측정
     * 
     * POST /api/ai/test/performance/custom
     * 
     * @param request 커스텀 요청
     * @return 측정 결과
     */
    @PostMapping("/performance/custom")
    public ResponseEntity<PerformanceResult> testCustomPerformance(
            @RequestBody CustomPerformanceRequest request) {

        PerformanceResult result = performanceTest.measureFullAnalysis(
                request.resumeText(),
                request.jdText(),
                request.candidateId());

        return ResponseEntity.ok(result);
    }

    /**
     * 커스텀 성능 측정 요청 DTO
     */
    public record CustomPerformanceRequest(
            String resumeText,
            String jdText,
            String candidateId
    ) {}
}
