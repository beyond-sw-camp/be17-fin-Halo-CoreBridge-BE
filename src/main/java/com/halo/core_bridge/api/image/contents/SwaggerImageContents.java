package com.halo.core_bridge.api.image.contents;

public class SwaggerImageContents {

    public static final String UPLOAD_RESPONSE = """
            {
              "success": true,
              "code": "20000",
              "message": "요청에 성공하였습니다.",
              "result": {
                "id": 1,
                "originalName": "test.jpg",
                "imageName": "some-uuid-test.jpg",
                "imagePath": "/images/some-uuid-test.jpg",
                "imageUrl": "http://localhost:8080/images/some-uuid-test.jpg",
                "imageSize": 12345
              }
            }
            """;

    public static final String GET_RESPONSE = """
            {
              "success": true,
              "code": "20000",
              "message": "요청에 성공하였습니다.",
              "result": "http://localhost:8080/images/some-uuid-test.jpg"
            }
            """;

    public static final String DELETE_RESPONSE = """
            {
              "success": true,
              "code": "20000",
              "message": "요청에 성공하였습니다.",
              "result": null
            }
            """;
}
