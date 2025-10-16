package com.halo.core_bridge.api.auth.contents;

public class SwaggerAuthContents {

    public static final String AUTH_CODE = """
            {
                "code":"312836",
                "email":"lesw1216@gmail.com"
            }
            """;

    public static final String RESPONSE_SUCCESS = """
            {
                "success": true,
                "code": 20000,
                "message": "요청에 성공하였습니다.",
                "results": "이메일 인증 성공"
            }
            """;

    public static final String RESPONSE_FAILED = """
            {
                "success": false,
                "code": 30002,
                "message": "유효하지 않은 인증번호 입니다.",
                "results": null
            }
            """;

    public static final String RESPONSE_SUCCESS_GET = """
            {
                 "success": true,
                 "code": 20000,
                 "message": "요청에 성공하였습니다.",
                 "results": "인증 번호 전송 성공"
             }
            """;

    public static final String RESPONSE_FAILED_GET = """
            {
                 "success": false,
                 "code": 20006,
                 "message": "중복된 이메일입니다. 다른 이메일을 사용해주세요.",
                 "results": null
             }
            """;
}
