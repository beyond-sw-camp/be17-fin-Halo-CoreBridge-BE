package com.halo.core_bridge.api.resume.contents;

public class SwaggerResumeContents {

    public static final String RESUME_RESPONSE = """
                {
                  "id": 1,
                  "appliedAt": "2025-10-05T14:30:00",
                  "description": "5년차 백엔드 개발자로 MSA 전환 프로젝트를 리드한 경험이 있습니다.",
                  "jobPostingId": 1,
                  "userId": 6,
                  "careers": [
                    {
                      "id": 1,
                      "companyName": "네이버",
                      "position": "백엔드 개발자",
                      "startDate": "2020-01-01T00:00:00",
                      "endDate": "2025-09-30T00:00:00"
                    },
                    {
                      "id": 2,
                      "companyName": "카카오",
                      "position": "백엔드 개발자",
                      "startDate": "2018-03-01T00:00:00",
                      "endDate": "2019-12-31T00:00:00"
                    }
                  ],
                  "certificates": [
                    {
                      "id": 1,
                      "name": "정보처리기사",
                      "acquiredDate": "2019-11"
                    },
                    {
                      "id": 2,
                      "name": "AWS Certified Solutions Architect",
                      "acquiredDate": "2022-03"
                    }
                  ],
                  "educations": [
                    {
                      "id": 1,
                      "schoolName": "서울대학교",
                      "major": "컴퓨터공학과",
                      "degree": "학사"
                    }
                  ],
                  "languages": [
                    {
                      "id": 1,
                      "name": "영어",
                      "testName": "TOEIC",
                      "languageName": "English",
                      "grade": "950",
                      "speakingLevel": "Advanced",
                      "testDate": "2024-08-15"
                    }
                  ],
                  "overseasExperiences": [
                    {
                      "id": 1,
                      "type": "어학연수",
                      "country": "미국",
                      "startDate": "2017-01-01",
                      "endDate": "2017-12-31",
                      "note": "캘리포니아 어학연수"
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
                    },
                    {
                      "id": 3,
                      "name": "Kubernetes"
                    },
                    {
                      "id": 4,
                      "name": "MySQL"
                    },
                    {
                      "id": 5,
                      "name": "Redis"
                    },
                    {
                      "id": 6,
                      "name": "AWS"
                    }
                  ]
                }
            """;

    public static final String RESUME_CREATE_REQUEST = """
                {
                  "description": "저는 백엔드 개발자로서 5년간의 경력을 가지고 있습니다.",
                  "jobPostingId": 1,
                  "careers": [],
                  "certificates": [],
                  "educations": [],
                  "languages": [],
                  "overseasExperiences": [],
                  "resumeSkills": []
                }
            """;

    public static final String RESUME_CREATE_RESPONSE = """
                {
                  "success": true,
                  "code": "20000",
                  "message": "요청에 성공하였습니다.",
                  "result": 1
                }
            """;

    public static final String RESUME_UPDATE_REQUEST = """
                {
                  "description": "업데이트된 이력서 설명입니다.",
                  "careers": [],
                  "certificates": [],
                  "educations": [],
                  "languages": [],
                  "overseasExperiences": [],
                  "resumeSkills": []
                }
            """;

    public static final String RESUME_UPDATE_RESPONSE = """
                {
                  "success": true,
                  "code": "20000",
                  "message": "요청에 성공하였습니다.",
                  "result": null
                }
            """;

    public static final String RESUME_DELETE_RESPONSE = """
                {
                  "success": true,
                  "code": "20000",
                  "message": "요청에 성공하였습니다.",
                  "result": null
                }
            """;

    public static final String RESUME_LIST_RESPONSE = """
                {
                  "success": true,
                  "code": 20000,
                  "message": "요청에 성공하였습니다.",
                  "results": [
                    {
                      "id": 1,
                      "appliedAt": "2025-10-05T14:30:00",
                      "description": "5년차 백엔드 개발자로 MSA 전환 프로젝트를 리드한 경험이 있습니다.",
                      "jobPostingId": 1,
                      "userId": 6,
                      "careers": [
                        {
                          "id": 1,
                          "companyName": "네이버",
                          "position": "백엔드 개발자",
                          "startDate": "2020-01-01T00:00:00",
                          "endDate": "2025-09-30T00:00:00"
                        },
                        {
                          "id": 2,
                          "companyName": "카카오",
                          "position": "백엔드 개발자",
                          "startDate": "2018-03-01T00:00:00",
                          "endDate": "2019-12-31T00:00:00"
                        }
                      ],
                      "certificates": [
                        {
                          "id": 1,
                          "name": "정보처리기사",
                          "acquiredDate": "2019-11"
                        },
                        {
                          "id": 2,
                          "name": "AWS Certified Solutions Architect",
                          "acquiredDate": "2022-03"
                        }
                      ],
                      "educations": [
                        {
                          "id": 1,
                          "schoolName": "서울대학교",
                          "major": "컴퓨터공학과",
                          "degree": "학사"
                        }
                      ],
                      "languages": [
                        {
                          "id": 1,
                          "name": "영어",
                          "testName": "TOEIC",
                          "languageName": "English",
                          "grade": "950",
                          "speakingLevel": "Advanced",
                          "testDate": "2024-08-15"
                        }
                      ],
                      "overseasExperiences": [
                        {
                          "id": 1,
                          "type": "어학연수",
                          "country": "미국",
                          "startDate": "2017-01-01",
                          "endDate": "2017-12-31",
                          "note": "캘리포니아 어학연수"
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
                        },
                        {
                          "id": 3,
                          "name": "Kubernetes"
                        },
                        {
                          "id": 4,
                          "name": "MySQL"
                        },
                        {
                          "id": 5,
                          "name": "Redis"
                        },
                        {
                          "id": 6,
                          "name": "AWS"
                        }
                      ]
                    },
                    {
                      "id": 2,
                      "appliedAt": "2025-10-07T10:15:00",
                      "description": "스타트업에서 3년간 Spring Boot로 다양한 서비스를 개발했습니다.",
                      "jobPostingId": 1,
                      "userId": 7,
                      "careers": [
                        {
                          "id": 3,
                          "companyName": "토스",
                          "position": "Spring 개발자",
                          "startDate": "2022-06-01T00:00:00",
                          "endDate": "2025-10-01T00:00:00"
                        }
                      ],
                      "certificates": [
                        {
                          "id": 3,
                          "name": "SQLD",
                          "acquiredDate": "2021-06"
                        }
                      ],
                      "educations": [
                        {
                          "id": 2,
                          "schoolName": "연세대학교",
                          "major": "소프트웨어학과",
                          "degree": "학사"
                        }
                      ],
                      "languages": [],
                      "overseasExperiences": [],
                      "resumeSkills": [
                        {
                          "id": 7,
                          "name": "Java"
                        },
                        {
                          "id": 8,
                          "name": "Spring Boot"
                        },
                        {
                          "id": 9,
                          "name": "PostgreSQL"
                        },
                        {
                          "id": 10,
                          "name": "Docker"
                        },
                        {
                          "id": 11,
                          "name": "Jenkins"
                        }
                      ]
                    },
                    {
                      "id": 3,
                      "appliedAt": "2025-10-18T16:20:00",
                      "description": "React와 TypeScript를 활용한 대규모 프로젝트 경험이 있습니다.",
                      "jobPostingId": 2,
                      "userId": 8,
                      "careers": [
                        {
                          "id": 4,
                          "companyName": "쿠팡",
                          "position": "프론트엔드 개발자",
                          "startDate": "2021-07-01T00:00:00",
                          "endDate": "2024-05-31T00:00:00"
                        }
                      ],
                      "certificates": [
                        {
                          "id": 4,
                          "name": "정보처리기사",
                          "acquiredDate": "2020-08"
                        }
                      ],
                      "educations": [
                        {
                          "id": 3,
                          "schoolName": "고려대학교",
                          "major": "컴퓨터학과",
                          "degree": "학사"
                        }
                      ],
                      "languages": [
                        {
                          "id": 2,
                          "name": "영어",
                          "testName": "TOEIC Speaking",
                          "languageName": "English",
                          "grade": "Level 7",
                          "speakingLevel": "Advanced",
                          "testDate": "2024-03-20"
                        }
                      ],
                      "overseasExperiences": [],
                      "resumeSkills": [
                        {
                          "id": 12,
                          "name": "React"
                        },
                        {
                          "id": 13,
                          "name": "TypeScript"
                        },
                        {
                          "id": 14,
                          "name": "Next.js"
                        },
                        {
                          "id": 15,
                          "name": "Redux"
                        },
                        {
                          "id": 16,
                          "name": "Tailwind CSS"
                        }
                      ]
                    }
                  ]
                }
            """;

    public static final String COVER_LETTER_CREATE_REQUEST = """
                [
                  {
                    "description": "자기소개서 항목 1에 대한 답변입니다.",
                    "resumeId": 1,
                    "coverLetterTitleId": 1
                  },
                  {
                    "description": "자기소개서 항목 2에 대한 답변입니다.",
                    "resumeId": 1,
                    "coverLetterTitleId": 2
                  }
                ]
            """;

    public static final String COVER_LETTER_CREATE_RESPONSE = """
                {
                  "success": true,
                  "code": "20000",
                  "message": "요청에 성공하였습니다.",
                  "result": [1, 2]
                }
            """;

    public static final String COVER_LETTER_LIST_RESPONSE = """
                {
                  "success": true,
                  "code": "20000",
                  "message": "요청에 성공하였습니다.",
                  "result": [
                    {
                      "id": 1,
                      "description": "자기소개서 항목 1에 대한 답변입니다.",
                      "resumeId": 1,
                      "coverLetterId": 1
                    },
                    {
                      "id": 2,
                      "description": "자기소개서 항목 2에 대한 답변입니다.",
                      "resumeId": 1,
                      "coverLetterId": 2
                    }
                  ]
                }
            """;
}
