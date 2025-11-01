package com.halo.core_bridge.api.auth.contents;

public class SwaggerAuthContents {

    public static final String SEND_AUTH_CODE_RESPONSE_SUCCESS = """
            {
               "success": true,
               "code": 20000,
               "message": "요청에 성공하였습니다.",
               "results": "인증 번호 전송 성공"
             }
            """;

    public static final String SEND_AUTH_CODE_RESPONSE_FAILED = """
            {
               "success": false,
               "code": 20006,
               "message": "중복된 이메일입니다. 다른 이메일을 사용해주세요.",
               "results": null
            }
            """;

    public static final String AUTH_CODE = """
            {
              "email": "test@corebridge.com",
              "code": "154401"
            }
            """;

    public static final String AUTH_CODE_AUTHENTICATE_RESPONSE_SUCCESS = """
            {
                "success": true,
                "code": 20000,
                "message": "요청에 성공하였습니다.",
                "results": "이메일 인증 성공"
            }
            """;

    public static final String AUTH_CODE_AUTHENTICATE_RESPONSE_FAILED = """
            {
                 "success": false,
                 "code": 30002,
                 "message": "유효하지 않은 인증번호 입니다.",
                 "results": null
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
