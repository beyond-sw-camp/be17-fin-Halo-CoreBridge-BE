package com.halo.core_bridge.api.auth.contents;

public class SwaggerAuthContents {

    public static final String RESPONSE_SUCCESS_GET = """
            {
              "isSuccess": true,
              "code": "COMMON200",
              "message": "요청에 성공하였습니다.",
              "result": "인증 번호 전송 성공"
            }
            """;

    public static final String RESPONSE_FAILED_GET = """
            {
              "isSuccess": false,
              "code": "USER401",
              "message": "이미 가입된 회원입니다.",
              "result": null
            }
            """;

    public static final String AUTH_CODE = """
            {
              "email": "test@corebridge.com",
              "authCode": "123456"
            }
            """;

    public static final String RESPONSE_SUCCESS = """
            {
              "isSuccess": true,
              "code": "COMMON200",
              "message": "요청에 성공하였습니다.",
              "result": "이메일 인증 성공"
            }
            """;

    public static final String RESPONSE_FAILED = """
            {
              "isSuccess": false,
              "code": "AUTH400",
              "message": "인증번호가 일치하지 않습니다.",
              "result": null
            }
            """;

    public static final String SEND_EMAIL_REQUEST = """
            {
              "email": "test@corebridge.com"
            }
            """;

    public static final String SEND_EMAIL_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": "비밀번호 재설정 링크 전송 성공"
            }
            """;

    public static final String RESET_PASSWORD_REQUEST = """
            {
              "email": "test@corebridge.com",
              "password": "newPassword123!",
              "token": "uuid"
            }
            """;

    public static final String RESET_PASSWORD_RESPONSE = """
            {
               "success": true,
               "code": 20000,
               "message": "요청에 성공하였습니다.",
               "results": "재설정 성공"
             }
            """;

    public static final String FIND_EMAIL_REQUEST = """
            {
              "name": "이상우",
              "phone": "010-5444-0853"
            }
            """;

    public static final String FIND_EMAIL_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "findEmail": "lesw1216@gmail.com"
              }
            }
            """;
}
