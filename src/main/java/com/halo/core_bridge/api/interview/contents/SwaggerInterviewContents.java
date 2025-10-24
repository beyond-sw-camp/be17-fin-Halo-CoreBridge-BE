package com.halo.core_bridge.api.interview.contents;

public class SwaggerInterviewContents {

    public static final String INTERVIEW_CREATE = """
            {
               "startDateTime": "2025-10-24T15:30:00",
               "status": "ONGOING",
               "description": "면접상세",
               "duration": 60,
               "recruiterProcessId": 1,
               "roomId": 1,
               "resumeId": 1
            }
            """;

    public static final String RESPONSE_SUCCESS = """
            {
                "success": true,
                "code": 20000,
                "message": "요청에 성공하였습니다.",
                "results": "면접 등록 성공"
            }
            """;
}
