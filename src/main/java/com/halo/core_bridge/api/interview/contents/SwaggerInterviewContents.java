package com.halo.core_bridge.api.interview.contents;

public class SwaggerInterviewContents {

    public static final String INTERVIEW_CREATE = """
            {
              "startDateTime": "2024-12-10T15:30:00",
              "duration": 60,
              "status": "ONGOING",
              "description": "1차 기술 면접입니다.",
              "resumeId": 1,
              "roomId": 1,
              "recruiterProcessId": 1
            }
            """;

    public static final String RESPONSE_SUCCESS = """
            {
                "isSuccess": true,
                "code": "COMMON201",
                "message": "요청에 성공하였습니다.",
                "result": "면접을 등록하였습니다."
             }
            """;
}
