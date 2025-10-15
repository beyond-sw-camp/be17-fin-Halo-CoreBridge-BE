package com.halo.core_bridge.api.jobposting.contents;

public class SwaggerJobPostingContents {

    // ✅ 채용공고 등록 요청 예시
    public static final String JOB_POSTING_CREATE_REQUEST = """
            {
              "title": "백엔드 개발자 채용",
              "description": "Spring Boot 기반 서버 개발자 모집",
              "employmentType": "정규직",
              "careerType": "경력",
              "minExperience": 2,
              "maxExperience": 5,
              "departmentId": 1,
              "applyStartDate": "2025-10-15T09:00:00",
              "applyEndDate": "2025-11-15T18:00:00",
              "hireEndDate": "2025-12-01T18:00:00",
              "skills": [
                "Java",
                "Spring Boot",
                "MySQL",
                "AWS"
              ]
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
                  "id": 3,
                  "title": "백엔드 개발자 채용",
                  "department": "플랫폼개발팀",
                  "employmentType": "정규직",
                  "applyEndDate": "2025-11-15T18:00:00"
                },
                {
                  "id": 4,
                  "title": "백엔드 개발자 채용",
                  "department": "플랫폼개발팀",
                  "employmentType": "정규직",
                  "applyEndDate": "2025-11-15T18:00:00"
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
}
