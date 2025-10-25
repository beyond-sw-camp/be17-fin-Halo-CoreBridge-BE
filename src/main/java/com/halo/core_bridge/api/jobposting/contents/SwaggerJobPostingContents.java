package com.halo.core_bridge.api.jobposting.contents;

public class SwaggerJobPostingContents {

    // ✅ 채용공고 등록 요청 예시
    public static final String JOB_POSTING_CREATE_REQUEST = """
            {
              "title": "시니어 프론트엔드 개발자",
              "employmentType": "정규직",
              "careerType": "신입",
              "minExperience": 5,
              "maxExperience": 10,
              "positionLevel": "시니어",
              "location": "서울 성수",
              "applyStartDate": "2025-10-10 09:00:00",
              "applyEndDate": "2025-10-29 18:00:00",
              "hireEndDate": "2025-10-31 18:00:00",
              "headcount": 3,
              "summary": "고객용 대시보드 및 관리자 콘솔의 프론트엔드 개발을 리드합니다.",
              "responsibilities": "Vue 3 + TypeScript 기반 신규 기능 개발, UI 아키텍처 설계, 성능 최적화, 코드 리뷰.",
              "requirements": "Vue 3, TypeScript 실무 5년 이상 경험. 상태관리(Pinia, Vuex) 및 REST API 연동 경험.",
              "preferred": "대규모 트래픽 대응 경험, SSR(Nuxt) 경험, 디자인 시스템 구축 경험.",
              "techStack": ["Vue", "TypeScript", "Pinia", "Vite", "TailwindCSS"],
              "recruitProcess": ["지원 완료", "서류 검토", "1차 면접", "2차 면접", "최종 합격"],
              "salaryType": "연봉",
              "salaryMin": 6500,
              "salaryMax": 8500,
              "salaryNegotiable": true,
              "workingHours": "09:00 ~ 18:00 (주 5일)",
              "benefits": "중식 제공, 자율 출퇴근, 재택근무 가능, 교육비 지원",
              "departmentId": 1,
              "contactName": "김채용",
              "contactEmail": "recruit@example.com",
              "additionalInfo": "포트폴리오 또는 GitHub 링크를 함께 제출해주세요."
            }
            """;

    // ✅ 채용공고 저장 완료 응답
    public static final String JOB_POSTING_CREATE_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": "저장완료"
            }
            """;

    // ✅ 채용공고 전체 조회 응답
    public static final String JOB_POSTING_LIST_RESPONSE = """
            {
                   "success": true,
                   "code": 20000,
                   "message": "요청에 성공하였습니다.",
                   "results": [
                       {
                           "id": 1,
                           "title": "시니어 프론트엔드 개발자",
                           "summaryText": "신입 · 정규직",
                           "departmentName": "플랫폼개발팀",
                           "employmentType": "정규직",
                           "careerType": "신입",
                           "status": "채용중",
                           "hireEndDate": "2025-10-31",
                           "dday": "D-6",
                           "applicantCount": 14,
                           "progressPercent": 71,
                           "processSummaries": [
                               {
                                   "stageName": "지원 완료",
                                   "count": 5,
                                   "orderIndex": 0
                               },
                               {
                                   "stageName": "서류 검토",
                                   "count": 3,
                                   "orderIndex": 1
                               },
                               {
                                   "stageName": "1차 면접",
                                   "count": 3,
                                   "orderIndex": 2
                               },
                               {
                                   "stageName": "최종 합격",
                                   "count": 3,
                                   "orderIndex": 3
                               }
                           ]
                       }
                   ]
               }
            """;

    // ✅ 채용공고 단건 상세 조회 응답
    public static final String JOB_POSTING_DETAIL_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "id": 4,
                "title": "백엔드 개발자 채용",
                "description": "Spring Boot 기반 서버 개발자 모집",
                "department": "플랫폼개발팀",
                "employmentType": "정규직",
                "skills": [
                  "Java",
                  "Spring Boot",
                  "MySQL",
                  "AWS"
                ],
                "applyStartDate": "2025-10-15 09:00:00",
                "applyEndDate": "2025-11-15 18:00:00"
              }
            }
            """;

    // ✅ 채용공고 상세조회 시 존재하지 않을 때
    public static final String JOB_POSTING_NOT_FOUND_RESPONSE = """
            {
              "success": false,
              "code": 70001,
              "message": "존재하지 않는 채용공고입니다.",
              "results": null
            }
            """;

    public static final String JOB_POSTINGS_NOT_FOUND_RESPONSE = """
            {
              "success": false,
              "code": 70002,
              "message": "등록된 채용공고가 없습니다",
              "results": null
            }
            """;

    // ✅ 유효성 검증 실패 응답 (입력 누락 등)
    public static final String JOB_POSTING_VALIDATION_ERROR_RESPONSE = """
            {
              "success": false,
              "code": 20000,
              "message": "입력값 예외가 발생했습니다. 올바른 값을 입력하세요.",
              "results": {
                "title": "제목은 필수 입력값입니다."
              }
            }
            """;

    public static final String JOB_POSTING_UPDATE_REQUEST = """
            {
              "title": "백엔드 개발자 (Spring Boot)",
              "description": "Spring Boot 기반 REST API 개발 및 운영 업무를 담당합니다.",
              "employmentType": "정규직",
              "careerType": "경력",
              "minExperience": 3,
              "maxExperience": 7,
              "departmentId": 2,
              "applyStartDate": "2025-10-20 09:00:00",
              "applyEndDate": "2025-11-30 18:00:00",
              "hireEndDate": "2025-12-10 18:00:00",
              "skills": [
                "Java",
                "Spring Boot",
                "AWS",
                "Docker"
              ]
            }
            """;

    public static final String JOB_POSTING_UPDATE_RESPONSE = """
                {
                "success": true,
                "code": 20000,
                "message": "요청에 성공하였습니다.",
                "results": "수정 완료"
                }
            """;

    public static final String JOB_POSTING_DELETE_RESPONSE = """
        {
          "success": true,
          "code": 20000,
          "message": "채용공고 삭제 완료",
          "result": null
        }
        """;
}
