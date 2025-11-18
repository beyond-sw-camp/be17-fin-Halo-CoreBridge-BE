package com.halo.core_bridge.api.schedule.jobposting.contents;

public class SwaggerJobPostingScheduleContents {

    public static final String CREATE_REQUEST = """
                    {
                      "applicants": 0,
                      "assignedTo": 1,
                      "benefits": "8888",
                      "daysLeft": 48,
                      "deadline": "2025-12-19",
                      "department": "개발팀",
                      "description": "저희 회사와 함께할 인재를 찾습니다!.\\n\\n가족같은 회사에서 지내고 싶으신분!\\n",
                      "endTime": "18:00",
                      "experience": "신입",
                      "final": 0,
                      "id": null,
                      "interview1": 0,
                      "interview2": 0,
                      "isUrgent": false,
                      "position": "frontend",
                      "postedDate": "2025-11-01",
                      "preferences": "- jest 사용 경험",
                      "progress": 0,
                      "requirements": "- React 개발 3년",
                      "responsibilities": "- React를 사용한 웹 프론트 개발\\n",
                      "screening": 0,
                      "startTime": "09:00",
                      "status": "recruiting",
                      "title": "프론트 엔드 개발자 채용 구합니다.",
                      "type": "정규직"
                    }
            """;

    public static final String CREATE_RESPONSE = """
                    {
                         "success": true,
                         "code": 20000,
                         "message": "요청에 성공하였습니다.",
                         "results": {
                             "id": 4,
                             "title": "프론트 엔드 개발자 채용 구합니다.",
                             "position": "frontend",
                             "department": "개발팀",
                             "experience": "신입",
                             "type": "정규직",
                             "assignedTo": 1,
                             "postedDate": "2025-11-01",
                             "deadline": "2025-12-19",
                             "startTime": "09:00:00",
                             "endTime": "18:00:00",
                             "status": "recruiting",
                             "description": "저희 회사와 함께할 인재를 찾습니다!.\\n\\n가족같은 회사에서 지내고 싶으신분!\\n",
                             "responsibilities": "- React를 사용한 웹 프론트 개발\\n",
                             "requirements": "- React 개발 3년",
                             "preferences": "- jest 사용 경험",
                             "benefits": "8888",
                             "applicants": 0,
                             "progress": 0,
                             "screening": 0,
                             "interview1": 0,
                             "interview2": 0,
                             "finalStage": 0,
                             "sharedWith": [],
                             "urgent": false
                         }
                     }
            """;

    public static final String UPDATE_REQUEST = """
                    {
                       "title": "[수정] 백엔드 개발자 채용",
                       "position": "백엔드 개발자",
                       "department": "개발팀",
                       "experience": "경력 5년 이상",
                       "type": "정규직",
                       "assignedTo": 1,
                       "postedDate": "2024-10-01",
                       "deadline": "2024-11-15",
                       "status": "interviewing",
                       "isUrgent": false
                     }
            """;

    public static final String UPDATE_RESPONSE = """
            {
               "success": true,
               "code": 20000,
               "message": "요청에 성공하였습니다.",
               "results": {
                 "id": 2,
                 "title": "[수정] 백엔드 개발자 채용",
                 "position": "백엔드 개발자",
                 "department": "개발팀",
                 "experience": "경력 5년 이상",
                 "type": "정규직",
                 "assignedTo": 1,
                 "postedDate": "2024-10-01",
                 "deadline": "2024-11-15",
                 "startTime": null,
                 "endTime": null,
                 "status": "interviewing",
                 "description": null,
                 "responsibilities": null,
                 "requirements": null,
                 "preferences": null,
                 "benefits": null,
                 "applicants": 18,
                 "progress": 15,
                 "screening": 12,
                 "interview1": 0,
                 "interview2": 0,
                 "finalStage": 0,
                 "sharedWith": [
                   3,
                   5
                 ],
                 "urgent": false
               }
             }
            """;

    public static final String DELETE_RESPONSE = """
                {
                  "isSuccess": true,
                  "code": "COMMON204",
                  "message": "요청에 성공하였습니다.",
                  "result": null
                }
            """;

    public static final String LIST_RESPONSE = """
                {
                   "success": true,
                   "code": 20000,
                   "message": "요청에 성공하였습니다.",
                   "results": [
                     {
                       "id": 1,
                       "title": "백엔드 개발자 시니어급 채용",
                       "position": "백엔드 개발자",
                       "department": "플랫폼개발팀",
                       "experience": "3-7년",
                       "type": "정규직",
                       "assignedTo": 1,
                       "postedDate": "2025-10-01",
                       "deadline": "2025-11-30",
                       "startTime": "09:00:00",
                       "endTime": "18:00:00",
                       "status": "recruiting",
                       "description": "대규모 트래픽을 처리하는 백엔드 시스템 개발",
                       "responsibilities": "API 설계 및 개발, MSA 아키텍처 설계, 데이터베이스 최적화",
                       "requirements": "Spring Boot 3년 이상, RDBMS 경험, RESTful API 설계 경험",
                       "preferences": "MSA 경험, Kubernetes 경험, 클라우드 경험",
                       "benefits": "4대보험, 유연근무제, 최신 장비 지원, 교육비 지원",
                       "applicants": 23,
                       "progress": 10,
                       "screening": 8,
                       "interview1": 3,
                       "interview2": 2,
                       "finalStage": 0,
                       "sharedWith": [
                         5,
                         4,
                         3
                       ],
                       "urgent": true
                     },
                     {
                       "id": 2,
                       "title": "프론트엔드 개발자 (React)",
                       "position": "프론트엔드 개발자",
                       "department": "웹서비스팀",
                       "experience": "2-5년",
                       "type": "정규직",
                       "assignedTo": 2,
                       "postedDate": "2025-10-15",
                       "deadline": "2025-11-15",
                       "startTime": "09:00:00",
                       "endTime": "18:00:00",
                       "status": "screening",
                       "description": "사용자 경험 최우선 웹 서비스 개발",
                       "responsibilities": "React 기반 웹 앱 개발, 반응형 UI 구현",
                       "requirements": "React 2년 이상, TypeScript 능숙",
                       "preferences": "Next.js 경험, 성능 최적화 경험",
                       "benefits": "재택근무 가능, 최신 장비 지원",
                       "applicants": 18,
                       "progress": 15,
                       "screening": 12,
                       "interview1": 0,
                       "interview2": 0,
                       "finalStage": 0,
                       "sharedWith": [
                         5,
                         3
                       ],
                       "urgent": false
                     },
                     {
                       "id": 3,
                       "title": "데이터 엔지니어",
                       "position": "데이터 엔지니어",
                       "department": "데이터플랫폼팀",
                       "experience": "3-10년",
                       "type": "정규직",
                       "assignedTo": 4,
                       "postedDate": "2025-09-01",
                       "deadline": "2025-10-31",
                       "startTime": "10:00:00",
                       "endTime": "19:00:00",
                       "status": "interviewing",
                       "description": "빅데이터 파이프라인 구축",
                       "responsibilities": "데이터 파이프라인 설계, ETL 개발",
                       "requirements": "Python/Scala 능숙, Spark 경험",
                       "preferences": "Airflow 경험, 클라우드 경험",
                       "benefits": "스톡옵션, 원격근무 가능",
                       "applicants": 12,
                       "progress": 8,
                       "screening": 5,
                       "interview1": 3,
                       "interview2": 2,
                       "finalStage": 0,
                       "sharedWith": [
                         1,
                         3
                       ],
                       "urgent": true
                     },
                     {
                       "id": 4,
                       "title": "프론트 엔드 개발자 채용 구합니다.",
                       "position": "frontend",
                       "department": "개발팀",
                       "experience": "신입",
                       "type": "정규직",
                       "assignedTo": 1,
                       "postedDate": "2025-11-01",
                       "deadline": "2025-12-19",
                       "startTime": "09:00:00",
                       "endTime": "18:00:00",
                       "status": "recruiting",
                       "description": "저희 회사와 함께할 인재를 찾습니다!.\\n\\n가족같은 회사에서 지내고 싶으신분!\\n",
                       "responsibilities": "- React를 사용한 웹 프론트 개발\\n",
                       "requirements": "- React 개발 3년",
                       "preferences": "- jest 사용 경험",
                       "benefits": "8888",
                       "applicants": 0,
                       "progress": 0,
                       "screening": 0,
                       "interview1": 0,
                       "interview2": 0,
                       "finalStage": 0,
                       "sharedWith": [],
                       "urgent": false
                     },
                     {
                       "id": 5,
                       "title": "프론트 엔드개발 채용공고",
                       "position": "frontend",
                       "department": "개발팀",
                       "experience": "신입",
                       "type": "정규직",
                       "assignedTo": 1,
                       "postedDate": "2025-11-01",
                       "deadline": "2025-11-29",
                       "startTime": "09:00:00",
                       "endTime": "18:00:00",
                       "status": "recruiting",
                       "description": "저희 회사와 함께하실 가족같은 분을 모집합니다.",
                       "responsibilities": "UI/UX 최적화\\nReact 개발",
                       "requirements": "Typescript 사용 가능자",
                       "preferences": "jest 사용 경험자\\nreact 사용 경험자\\n운전면허 소지자",
                       "benefits": "점심 식대 지원\\n3번째 주 금요일 조기 퇴근",
                       "applicants": 0,
                       "progress": 0,
                       "screening": 0,
                       "interview1": 0,
                       "interview2": 0,
                       "finalStage": 0,
                       "sharedWith": [],
                       "urgent": false
                     }
                   ]
                 }
            """;

    public static final String GET_RESPONSE = """
                {
                   "success": true,
                   "code": 20000,
                   "message": "요청에 성공하였습니다.",
                   "results": {
                     "id": 1,
                     "title": "백엔드 개발자 시니어급 채용",
                     "position": "백엔드 개발자",
                     "department": "플랫폼개발팀",
                     "experience": "3-7년",
                     "type": "정규직",
                     "assignedTo": 1,
                     "postedDate": "2025-10-01",
                     "deadline": "2025-11-30",
                     "startTime": "09:00:00",
                     "endTime": "18:00:00",
                     "status": "recruiting",
                     "description": "대규모 트래픽을 처리하는 백엔드 시스템 개발",
                     "responsibilities": "API 설계 및 개발, MSA 아키텍처 설계, 데이터베이스 최적화",
                     "requirements": "Spring Boot 3년 이상, RDBMS 경험, RESTful API 설계 경험",
                     "preferences": "MSA 경험, Kubernetes 경험, 클라우드 경험",
                     "benefits": "4대보험, 유연근무제, 최신 장비 지원, 교육비 지원",
                     "applicants": 23,
                     "progress": 10,
                     "screening": 8,
                     "interview1": 3,
                     "interview2": 2,
                     "finalStage": 0,
                     "sharedWith": [
                       4,
                       3,
                       5
                     ],
                     "urgent": true
                    }
                }
            """;

    public static final String CALENDAR_RESPONSE = """
                {
                   "success": true,
                   "code": 20000,
                   "message": "요청에 성공하였습니다.",
                   "results": {
                     "2025-10-01": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-02": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-03": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-04": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-05": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-06": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-07": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-08": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-09": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-10": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-11": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-12": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-13": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-14": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-15": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-16": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-17": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-18": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-19": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-20": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-21": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-22": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-23": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-24": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-25": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-26": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-27": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-28": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-29": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-30": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ],
                     "2025-10-31": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 시니어급 채용",
                         "status": "recruiting",
                         "urgent": true,
                         "department": "플랫폼개발팀"
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "status": "screening",
                         "urgent": false,
                         "department": "웹서비스팀"
                       },
                       {
                         "id": 3,
                         "title": "데이터 엔지니어",
                         "status": "interviewing",
                         "urgent": true,
                         "department": "데이터플랫폼팀"
                       }
                     ]
                   }
                 }
            """;

    public static final String SHARE_REQUEST = """
                {
                  "userIds": [2, 3]
                }
            """;

    public static final String SHARE_RESPONSE = """
                {
                  "success": true,
                  "code": 20000,
                  "message": "요청에 성공하였습니다.",
                  "results": "공유 완료"
                }
            """;

    public static final String BULK_SHARE_REQUEST = """
                {
                  "jobs": [1, 2],
                  "members": [2, 3]
                }
            """;

    public static final String BULK_SHARE_RESPONSE = """
                {
                   "success": true,
                   "code": 20000,
                   "message": "요청에 성공하였습니다.",
                   "results": null
                 }
            """;
}
