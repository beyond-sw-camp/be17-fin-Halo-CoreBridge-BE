package com.halo.core_bridge.api.jobposting.contents;

public class SwaggerPublicJobPostingContents {

    public static final String GET_PUBLIC_JOB_POSTING_LIST_RESPONSE = """
                {
                   "success": true,
                   "code": 20000,
                   "message": "요청에 성공하였습니다.",
                   "results": {
                     "jobs": [
                       {
                         "id": 1,
                         "title": "백엔드 개발자 (Spring Boot)",
                         "summary": "글로벌 서비스를 지원하는 대규모 백엔드 시스템을 개발합니다.",
                         "experience": "경력 3년차 ~ 7년차",
                         "location": "서울 강남구",
                         "deadline": "D-28",
                         "department": "개발",
                         "views": 1
                       },
                       {
                         "id": 2,
                         "title": "프론트엔드 개발자 (React)",
                         "summary": "사용자 경험을 최우선으로 하는 웹 서비스 프론트엔드를 개발합니다.",
                         "experience": "경력 2년차 ~ 5년차",
                         "location": "서울 강남구",
                         "deadline": "D-13",
                         "department": "개발",
                         "views": 1
                       },
                       {
                         "id": 3,
                         "title": "iOS 앱 개발자",
                         "summary": "수백만 사용자가 사용하는 모바일 앱을 개발합니다.",
                         "experience": "신입",
                         "location": "서울 강남구",
                         "deadline": "D-48",
                         "department": "개발",
                         "views": 1
                       },
                       {
                         "id": 4,
                         "title": "데이터 엔지니어",
                         "summary": "빅데이터 파이프라인을 구축하고 데이터 인프라를 관리합니다.",
                         "experience": "경력 3년차 ~ 10년차",
                         "location": "서울 강남구",
                         "deadline": "마감",
                         "department": "개발",
                         "views": 1
                       },
                       {
                         "id": 5,
                         "title": "UI/UX 디자이너",
                         "summary": "사용자 중심의 직관적인 인터페이스를 디자인합니다.",
                         "experience": "경력 2년차 ~ 5년차",
                         "location": "서울 강남구",
                         "deadline": "D-23",
                         "department": "디자인",
                         "views": 1
                       },
                       {
                         "id": 6,
                         "title": "프로덕트 매니저 (PM)",
                         "summary": "데이터 기반으로 제품 전략을 수립하고 실행합니다.",
                         "experience": "경력 4년차 ~ 8년차",
                         "location": "서울 서초구",
                         "deadline": "D-28",
                         "department": "기획",
                         "views": 1
                       },
                       {
                         "id": 7,
                         "title": "백엔드 개발 인턴",
                         "summary": "실무 중심의 백엔드 개발 인턴십 프로그램입니다.",
                         "experience": "신입",
                         "location": "서울 강남구",
                         "deadline": "D-43",
                         "department": "개발",
                         "views": 1
                       }
                     ]
                   }
                 }
            """;
}
