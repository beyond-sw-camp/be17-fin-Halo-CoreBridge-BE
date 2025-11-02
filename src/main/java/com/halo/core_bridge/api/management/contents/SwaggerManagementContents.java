package com.halo.core_bridge.api.management.contents;

public class SwaggerManagementContents {

    public static final String MANAGEMENT_GET_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "jobPostingId": 1,
                "stages": [
                  {
                    "id": 1,
                    "name": "서류전형",
                    "colorCode": "BLUE",
                    "applicants": []
                  },
                  {
                    "id": 2,
                    "name": "코딩테스트",
                    "colorCode": "PURPLE",
                    "applicants": [
                      {
                        "id": 2,
                        "name": "윤재현",
                        "experience": 3.3,
                        "daysSinceApplied": 25
                      }
                    ]
                  },
                  {
                    "id": 3,
                    "name": "1차 기술면접",
                    "colorCode": "ORANGE",
                    "applicants": [
                      {
                        "id": 1,
                        "name": "강수빈",
                        "experience": 7.6,
                        "daysSinceApplied": 27
                      }
                    ]
                  },
                  {
                    "id": 4,
                    "name": "2차 컬처핏면접",
                    "colorCode": "PINK",
                    "applicants": [
                      {
                        "id": 10,
                        "name": "황동혁",
                        "experience": 7.8,
                        "daysSinceApplied": 24
                      }
                    ]
                  },
                  {
                    "id": 5,
                    "name": "최종합격",
                    "colorCode": "RED",
                    "applicants": []
                  }
                ]
              }
            }
            """;

    public static final String UPDATE_PROCESS_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": "단계 변경 완료"
            }
            """;
}
