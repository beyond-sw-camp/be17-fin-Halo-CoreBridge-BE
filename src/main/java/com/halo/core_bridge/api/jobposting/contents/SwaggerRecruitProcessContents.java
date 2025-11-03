package com.halo.core_bridge.api.jobposting.contents;

public class SwaggerRecruitProcessContents {

    public static final String RECRUITER_ADD_REQUEST = """
                {
                    "name": "서류 전형",
                    "colorCode": "PURPLE",
                    "jobPostingId": 1
                }
            """;

    public static final String RECRUITER_CHANGE_ORDER_REQUEST = """
                 {
                    "processId": 5,
                    "jobPostingId": 1,
                    "fromIdx": 5,
                    "toIdx": 1
                }
            """;

    public static final String RECRUITER_UPDATE_REQUEST = """
                {
                    "name": "3차 면접",
                    "colorCode": "PURPLE",
                    "jobPostingId": 1
                }
            """;

    public static final String RECRUITER_GET_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "recruitProcesses": [
                  {
                    "id": 1,
                    "name": "서류전형",
                    "colorCode": {
                      "name": "BLUE",
                      "label": "파랑",
                      "code": "blue-500"
                    },
                    "orderIdx": 1
                  },
                  {
                    "id": 2,
                    "name": "코딩테스트",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 2
                  },
                  {
                    "id": 3,
                    "name": "1차 기술면접",
                    "colorCode": {
                      "name": "ORANGE",
                      "label": "주황",
                      "code": "orange-500"
                    },
                    "orderIdx": 3
                  },
                  {
                    "id": 4,
                    "name": "2차 컬처핏면접",
                    "colorCode": {
                      "name": "PINK",
                      "label": "분홍",
                      "code": "pink-500"
                    },
                    "orderIdx": 4
                  },
                  {
                    "id": 5,
                    "name": "최종합격",
                    "colorCode": {
                      "name": "RED",
                      "label": "빨강",
                      "code": "red-500"
                    },
                    "orderIdx": 5
                  }
                ]
              }
            }
            """;

    public static final String RECRUITER_ADD_SUCCESS_RESPONSE = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "recruitProcesses": [
                  {
                    "id": 1,
                    "name": "서류전형",
                    "colorCode": {
                      "name": "BLUE",
                      "label": "파랑",
                      "code": "blue-500"
                    },
                    "orderIdx": 1
                  },
                  {
                    "id": 2,
                    "name": "코딩테스트",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 2
                  },
                  {
                    "id": 3,
                    "name": "1차 기술면접",
                    "colorCode": {
                      "name": "ORANGE",
                      "label": "주황",
                      "code": "orange-500"
                    },
                    "orderIdx": 3
                  },
                  {
                    "id": 4,
                    "name": "2차 컬처핏면접",
                    "colorCode": {
                      "name": "PINK",
                      "label": "분홍",
                      "code": "pink-500"
                    },
                    "orderIdx": 4
                  },
                  {
                    "id": 32,
                    "name": "3차 면접",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 5
                  },
                  {
                    "id": 5,
                    "name": "최종합격",
                    "colorCode": {
                      "name": "RED",
                      "label": "빨강",
                      "code": "red-500"
                    },
                    "orderIdx": 6
                  }
                ]
              }
            }
            """;

    public static final String RECRUITER_CHANGE_ORDER_RESPONSE_SUCCESS = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "recruitProcesses": [
                  {
                    "id": 5,
                    "name": "최종합격",
                    "colorCode": {
                      "name": "RED",
                      "label": "빨강",
                      "code": "red-500"
                    },
                    "orderIdx": 1
                  },
                  {
                    "id": 1,
                    "name": "서류전형",
                    "colorCode": {
                      "name": "BLUE",
                      "label": "파랑",
                      "code": "blue-500"
                    },
                    "orderIdx": 2
                  },
                  {
                    "id": 2,
                    "name": "코딩테스트",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 2
                  },
                  {
                    "id": 3,
                    "name": "1차 기술면접",
                    "colorCode": {
                      "name": "ORANGE",
                      "label": "주황",
                      "code": "orange-500"
                    },
                    "orderIdx": 3
                  },
                  {
                    "id": 4,
                    "name": "2차 컬처핏면접",
                    "colorCode": {
                      "name": "PINK",
                      "label": "분홍",
                      "code": "pink-500"
                    },
                    "orderIdx": 4
                  }
                ]
              }
            }
            """;

    public static final String RECRUITER_UPDATE_RESPONSE_SUCCESS = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "recruitProcesses": [
                  {
                    "id": 1,
                    "name": "3차 면접",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 1
                  },
                  {
                    "id": 2,
                    "name": "코딩테스트",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 2
                  },
                  {
                    "id": 3,
                    "name": "1차 기술면접",
                    "colorCode": {
                      "name": "ORANGE",
                      "label": "주황",
                      "code": "orange-500"
                    },
                    "orderIdx": 3
                  },
                  {
                    "id": 4,
                    "name": "2차 컬처핏면접",
                    "colorCode": {
                      "name": "PINK",
                      "label": "분홍",
                      "code": "pink-500"
                    },
                    "orderIdx": 4
                  },
                  {
                    "id": 5,
                    "name": "최종합격",
                    "colorCode": {
                      "name": "RED",
                      "label": "빨강",
                      "code": "red-500"
                    },
                    "orderIdx": 5
                  }
                ]
              }
            }
            """;

    public static final String RECRUITER_DELETE_RESPONSE_SUCCESS = """
            {
              "success": true,
              "code": 20000,
              "message": "요청에 성공하였습니다.",
              "results": {
                "recruitProcesses": [
                  {
                    "id": 1,
                    "name": "서류전형",
                    "colorCode": {
                      "name": "BLUE",
                      "label": "파랑",
                      "code": "blue-500"
                    },
                    "orderIdx": 1
                  },
                  {
                    "id": 2,
                    "name": "코딩테스트",
                    "colorCode": {
                      "name": "PURPLE",
                      "label": "보라",
                      "code": "purple-500"
                    },
                    "orderIdx": 2
                  },
                  {
                    "id": 3,
                    "name": "1차 기술면접",
                    "colorCode": {
                      "name": "ORANGE",
                      "label": "주황",
                      "code": "orange-500"
                    },
                    "orderIdx": 3
                  },
                  {
                    "id": 4,
                    "name": "2차 컬처핏면접",
                    "colorCode": {
                      "name": "PINK",
                      "label": "분홍",
                      "code": "pink-500"
                    },
                    "orderIdx": 4
                  },
                  {
                    "id": 5,
                    "name": "최종합격",
                    "colorCode": {
                      "name": "RED",
                      "label": "빨강",
                      "code": "red-500"
                    },
                    "orderIdx": 5
                  }
                ]
              }
            }
            """;

    public static final String RECRUITER_DELETE_RESPONSE_FAILED = """
            {
              "success": false,
              "code": 74002,
              "message": "채용 프로세스에 지원자가 존재하면 삭제 할 수 없습니다.",
              "results": null
            }
            """;
}
