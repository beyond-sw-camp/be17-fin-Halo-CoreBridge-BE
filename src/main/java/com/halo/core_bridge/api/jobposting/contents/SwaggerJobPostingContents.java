package com.halo.core_bridge.api.jobposting.contents;

public class SwaggerJobPostingContents {

    // 채용공고 등록 요청 예시
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
                "recruitProcess": [
                  { "name": "지원 완료", "color": "BLUE", "orderIdx": 1 },
                  { "name": "서류 검토", "color": "BLUE", "orderIdx": 2 },
                  { "name": "1차 면접", "color": "BLUE", "orderIdx": 3 },
                  { "name": "2차 면접", "color": "PURPLE", "orderIdx": 4 },
                  { "name": "최종 합격", "color": "PURPLE", "orderIdx": 5 }
                ],
                "coverLetterTitle": [
                  { "title" : "지원동기", "subtitle" : "해당부서에 지원하게 된 이유를 기술해주세요"}
                ],
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

    // 채용공고 저장 완료 응답
    public static final String JOB_POSTING_CREATE_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": "저장완료"
            }
            """;

    // 채용공고 전체 조회 응답
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

    // 채용공고 단건 상세 조회 응답
    public static final String JOB_POSTING_DETAIL_RESPONSE = """
            {
                  "success": true,
                  "code": 20000,
                  "message": "요청에 성공하였습니다.",
                  "results": {
                      "id": 3,
                      "summary": "CoreBridge SaaS 플랫폼 디자인 전반 담당",
                      "responsibilities": "UI/UX 설계 및 서비스 리디자인 프로젝트 수행",
                      "requirements": "Figma, Photoshop, Illustrator 숙련자",
                      "preferred": "프로덕트 디자인, 브랜드 디자인 모두 가능한 멀티형 디자이너\\n  ㅁㄴㅇ;럼;ㅣㅏ넝히ㅏ;먼이;ㅏㅓ라ㅣ;ㅁ넝;ㅏㅣㅓㅁㄴ;ㅣ어리ㅏ;ㅁ너아ㅣㄹ@:\\n  ㅁ니ㅏㅇ;러;미넝ㅎ;ㅏㅣ먼아ㅣ허\\n  ㅁㄴ아ㅣ험;ㅣ나ㅓ하ㅣㅁ넝ㅎ\\n  ㅁ날허;미ㅏㄴ렇;ㅏㅣ머라ㅣㅎ",
                      "benefits": "점심 지원, 유연근무, 도서구입비 지원",
                      "additionalInfo": "포트폴리오 필수, Figma 협업 경험 우대",
                      "status": "마감",
                      "createDate": "2025-10-27 07:41:04",
                      "applyStartDate": "2025-10-10 09:00:00",
                      "applyEndDate": "2025-10-10 18:00:00",
                      "hireEndDate": "2025-10-26 18:00:00",
                      "headCount": 4,
                      "applicantCount": null,
                      "skills": [
                          "Figma",
                          "Illustrator",
                          "Photoshop",
                          "Zeplin"
                      ],
                      "recruitProcesses": [
                          {
                              "id": 11,
                              "name": "지원 완료",
                              "colorCode": "blue-500",
                              "orderIdx": 1
                          },
                          {
                              "id": 12,
                              "name": "서류 검토",
                              "colorCode": "orange-500",
                              "orderIdx": 2
                          },
                          {
                              "id": 13,
                              "name": "1차 면접",
                              "colorCode": "purple-500",
                              "orderIdx": 3
                          },
                          {
                              "id": 14,
                              "name": "2차 면접",
                              "colorCode": "red-500",
                              "orderIdx": 4
                          },
                          {
                              "id": 15,
                              "name": "최종 합격",
                              "colorCode": "pink-500",
                              "orderIdx": 5
                          }
                      ],
                      "workingHours": "10:00 ~ 19:00 (주 5일)",
                      "location": "서울 강남",
                      "contactName": "이디자인",
                      "contactEmail": "design@halo.com"
                  }
              }
            """;

    public static final String JOB_POSTING_BASIC_RESPONSE = """
            {
                    "success": true,
                    "code": 20000,
                    "message": "요청에 성공하였습니다.",
                    "results": {
                        "id": 1,
                        "title": "시니어 프론트엔드 개발자",
                        "status": "채용중",
                        "departmentName": "플랫폼개발팀",
                        "employmentType": "정규직",
                        "location": "서울 성수",
                        "careerType": "신입",
                        "minExperience": 5,
                        "maxExperience": 10,
                        "skills": [
                            "JAVA",
                            "Vue",
                            "TypeScript",
                            "Pinia",
                            "Vite",
                            "TailwindCSS"
                        ],
                        "salaryType": "연봉",
                        "salaryMin": 6500,
                        "salaryMax": 8500,
                        "salaryNegotiable": true
                    }
                }
            """;

    // 채용공고 상세조회 시 존재하지 않을 때
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

    // 유효성 검증 실패 응답 (입력 누락 등)
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

    public static final String APPLICANT_RESPONSE = """
            {
                "success": true,
                "code": 20000,
                "message": "요청에 성공하였습니다.",
                "results": [
                    {
                        "name": "강수빈",
                        "email": "subin.kang@email.com",
                        "careerType": "경력",
                        "skills": [
                            "Java",
                            "Spring Boot",
                            "Kubernetes",
                            "MySQL",
                            "Redis",
                            "AWS"
                        ],
                        "degree": "학사",
                        "certificateCount": 2,
                        "applyDate": "2025-11-01",
                        "stageName": "1차 기술면접"
                    },
                    {
                        "name": "윤재현",
                        "email": "jaehyun.yoon@email.com",
                        "careerType": "경력",
                        "skills": [
                            "Java",
                            "Spring Boot",
                            "PostgreSQL",
                            "Docker",
                            "Jenkins"
                        ],
                        "degree": "학사",
                        "certificateCount": 1,
                        "applyDate": "2025-11-01",
                        "stageName": "코딩테스트"
                    },
                    {
                        "name": "황동혁",
                        "email": "donghyuk.hwang@email.com",
                        "careerType": "경력",
                        "skills": [
                            "Java",
                            "Spring Boot",
                            "Redis",
                            "Kafka",
                            "MySQL",
                            "AWS"
                        ],
                        "degree": "학사",
                        "certificateCount": 1,
                        "applyDate": "2025-11-01",
                        "stageName": "2차 컬처핏면접"
                    }
                ]
            }
            """;
}
