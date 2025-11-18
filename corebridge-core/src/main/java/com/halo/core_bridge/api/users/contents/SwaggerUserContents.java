package com.halo.core_bridge.api.users.contents;

public class SwaggerUserContents {

    public static final String USER_CREATE = """
            {
              "email":"lesw1216@gmail.com",
              "password":"12345678",
              "name":"test",
              "phone":"010-1234-5678",
              "birth":"1996-12-16",
              "gender":"Male"
            }
            """;

    public static final String RESPONSE_SUCCESS = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": "회원 가입 성공"
            }
            """;

    public static final String RESPONSE_FAILED = """
            {
              "success": false,
              "code": 20001,
              "message": "입력값 예외가 발생했습니다. 올바른 값을 입력하세요.",
              "results": {
                  "password": "비밀번호를 입력하세요.",
                  "gender": "성별을 선택하세요.",
                  "phone": "연락처를 입력하세요.",
                  "name": "이름을 입력하세요.",
                  "birth": "생년월일을 입력하세요.",
                  "email": "이메일을 입력하세요."
              }
            }
            """;

    public static final String USER_DETAIL_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "name": "이상우",
                "email": "lesw1216@gmail.com",
                "gender": "남성",
                "phone": "010-5444-0853",
                "birth": "1996-12-16"
              }
            }
            """;

    public static final String LOGOUT_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": null
            }
            """;

    public static final String RESUME_USER_INFO_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "name": "이상우",
                "email": "lesw1216@gmail.com",
                "gender": "남성",
                "phone": "010-5444-0853",
                "birth": "1996-12-16"
              }
            }
            """;
}
