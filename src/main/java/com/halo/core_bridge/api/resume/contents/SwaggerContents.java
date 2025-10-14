package com.halo.core_bridge.api.resume.contents;

public class SwaggerContents {
    // 이력서 생성 요청
    public static final String RESUME_CREATE_REQUEST = """
            {
              "description": "저는 백엔드 개발자로서 5년간의 경력을 가지고 있습니다.",
              "jobPostingId": 1,
              "careers": [
                {
                  "companyName": "ABC 주식회사",
                  "position": "백엔드 개발자",
                  "startDate": "2020-01-01T09:00:00",
                  "endDate": "2022-12-31T18:00:00"
                },
                {
                  "companyName": "XYZ 테크",
                  "position": "시니어 백엔드 개발자",
                  "startDate": "2023-01-01T09:00:00",
                  "endDate": null
                }
              ],
              "certificates": [
                {
                  "name": "정보처리기사",
                  "acquiredDate": "2019-05-10"
                }
              ],
              "educations": [
                {
                  "schoolName": "한국대학교",
                  "major": "컴퓨터공학",
                  "degree": "학사"
                }
              ],
              "languages": [
                {
                  "name": "TOEIC",
                  "testName": "TOEIC",
                  "languageName": "영어",
                  "grade": "900",
                  "speakingLevel": "상",
                  "testDate": "2021-03-15"
                }
              ],
              "overseasExperiences": [
                {
                  "type": "교환학생",
                  "country": "미국",
                  "startDate": "2018-09-01",
                  "endDate": "2019-02-28",
                  "note": "미국 캘리포니아 주립대학교 교환학생"
                }
              ],
              "resumeSkills": [
                {
                  "name": "Java"
                },
                {
                  "name": "Spring Boot"
                }
              ]
            }
            """;

    // 이력서 수정 요청
    public static final String RESUME_UPDATE_REQUEST = """
            {
              "description": "업데이트된 이력서 설명입니다.",
              "careers": [
                {
                  "id": 1,
                  "companyName": "ABC 주식회사",
                  "position": "백엔드 개발자",
                  "startDate": "2020-01-01T09:00:00",
                  "endDate": "2022-12-31T18:00:00"
                },
                {
                  "id": 2,
                  "companyName": "XYZ 테크",
                  "position": "시니어 백엔드 개발자",
                  "startDate": "2023-01-01T09:00:00",
                  "endDate": null
                }
              ],
              "certificates": [
                {
                  "id": 1,
                  "name": "정보처리기사",
                  "acquiredDate": "2019-05-10"
                }
              ],
              "educations": [
                {
                  "id": 1,
                  "schoolName": "한국대학교",
                  "major": "컴퓨터공학",
                  "degree": "학사"
                }
              ],
              "languages": [
                {
                  "id": 1,
                  "name": "TOEIC",
                  "testName": "TOEIC",
                  "languageName": "영어",
                  "grade": "900",
                  "speakingLevel": "상",
                  "testDate": "2021-03-15"
                }
              ],
              "overseasExperiences": [
                {
                  "id": 1,
                  "type": "교환학생",
                  "country": "미국",
                  "startDate": "2018-09-01",
                  "endDate": "2019-02-28",
                  "note": "미국 캘리포니아 주립대학교 교환학생"
                }
              ],
              "resumeSkills": [
                {
                  "id": 1,
                  "name": "Java"
                },
                {
                  "id": 2,
                  "name": "Spring Boot"
                }
              ]
            }
            """;

    // 이력서 조회 응답
    public static final String RESUME_RESPONSE = """
            {
              "id": 1,
              "appliedAt": "2025-10-14T10:00:00",
              "description": "저는 백엔드 개발자로서 5년간의 경력을 가지고 있습니다.",
              "jobPostingId": 1,
              "userId": 10,
              "careers": [
                {
                  "id": 1,
                  "companyName": "ABC 주식회사",
                  "position": "백엔드 개발자",
                  "startDate": "2020-01-01T09:00:00",
                  "endDate": "2022-12-31T18:00:00"
                },
                {
                  "id": 2,
                  "companyName": "XYZ 테크",
                  "position": "시니어 백엔드 개발자",
                  "startDate": "2023-01-01T09:00:00",
                  "endDate": null
                }
              ],
              "certificates": [
                {
                  "id": 1,
                  "name": "정보처리기사",
                  "acquiredDate": "2019-05-10"
                }
              ],
              "educations": [
                {
                  "id": 1,
                  "schoolName": "한국대학교",
                  "major": "컴퓨터공학",
                  "degree": "학사"
                }
              ],
              "languages": [
                {
                  "id": 1,
                  "name": "TOEIC",
                  "testName": "TOEIC",
                  "languageName": "영어",
                  "grade": "900",
                  "speakingLevel": "상",
                  "testDate": "2021-03-15"
                }
              ],
              "overseasExperiences": [
                {
                  "id": 1,
                  "type": "교환학생",
                  "country": "미국",
                  "startDate": "2018-09-01",
                  "endDate": "2019-02-28",
                  "note": "미국 캘리포니아 주립대학교 교환학생"
                }
              ],
              "resumeSkills": [
                {
                  "id": 1,
                  "name": "Java"
                },
                {
                  "id": 2,
                  "name": "Spring Boot"
                }
              ]
            }
            """;
}