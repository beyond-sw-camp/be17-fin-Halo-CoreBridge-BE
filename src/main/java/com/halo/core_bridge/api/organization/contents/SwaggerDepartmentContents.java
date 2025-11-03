package com.halo.core_bridge.api.organization.contents;

public class SwaggerDepartmentContents {

    public static final String GET_DEPARTMENT_LIST_RESPONSE = """
            {
               "success": true,
               "code": 20000,
               "message": "요청에 성공하였습니다.",
               "results": [
                 {
                   "id": 1,
                   "name": "플랫폼개발팀"
                 },
                 {
                   "id": 2,
                   "name": "웹서비스팀"
                 },
                 {
                   "id": 3,
                   "name": "앱개발팀"
                 },
                 {
                   "id": 4,
                   "name": "데이터플랫폼팀"
                 },
                 {
                   "id": 5,
                   "name": "UX디자인팀"
                 },
                 {
                   "id": 6,
                   "name": "브랜드디자인팀"
                 },
                 {
                   "id": 7,
                   "name": "프로덕트기획팀"
                 },
                 {
                   "id": 8,
                   "name": "전략기획실"
                 },
                 {
                   "id": 9,
                   "name": "그로스마케팅팀"
                 },
                 {
                   "id": 10,
                   "name": "콘텐츠마케팅팀"
                 }
               ]
             }
            """;
}
