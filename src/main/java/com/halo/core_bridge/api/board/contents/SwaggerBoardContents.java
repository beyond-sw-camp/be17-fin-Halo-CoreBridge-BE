package com.halo.core_bridge.api.board.contents;

public class SwaggerBoardContents {

    public static final String CREATE_REQUEST = """
            {
              "title": "첫 번째 공지",
              "contents": "CoreBridge 공지사항 본문입니다.",
              "userId": 1
            }
            """;

    public static final String CREATE_RESPONSE = """
            {
              "isSuccess": true,
              "code": "COMMON201",
              "message": "요청에 성공하였습니다.",
              "result": {
                "id": 1,
                "title": "첫 번째 공지",
                "contents": "CoreBridge 공지사항 본문입니다.",
                "likeCount": 0,
                "viewCount": 0,
                "user": {
                  "id": 1
                }
              }
            }
            """;

    public static final String READ_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "id": 1,
                "title": "2025년 하반기 신입 공채 안내",
                "contents": "안녕하세요. 인사팀입니다.\\n2025년 하반기 신입 공채를 다음과 같이 진행합니다.\\n\\n- 접수기간: 2025.11.01 ~ 11.30\\n- 전형절차: 서류 > 코딩테스트 > 면접 > 최종합격\\n- 채용분야: 개발, 디자인, 기획\\n\\n많은 관심 부탁드립니다.",
                "writer": "김민준"
              }
            }
            """;

    public static final String UPDATE_REQUEST = """
            {
              "title": "수정된 제목",
              "contents": "수정된 본문 내용입니다."
            }
            """;

    public static final String UPDATE_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": null
            }
            """;

    public static final String DELETE_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": null
            }
            """;

    public static final String LIST_RESPONSE = """
            {
              "isSuccess": true,
              "code": "COMMON200",
              "message": "요청에 성공하였습니다.",
              "result": [
                {
                  "id": 1,
                  "title": "첫 번째 공지",
                  "contents": "CoreBridge 공지사항 본문입니다.",
                  "likeCount": 0,
                  "viewCount": 0,
                  "user": {
                    "id": 1
                  }
                },
                {
                  "id": 2,
                  "title": "두 번째 공지",
                  "contents": "두 번째 공지사항 본문입니다.",
                  "likeCount": 5,
                  "viewCount": 10,
                  "user": {
                    "id": 1
                  }
                }
              ]
            }
            """;
}
