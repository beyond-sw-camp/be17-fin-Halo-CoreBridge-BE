package com.halo.core_bridge.api.pdf.contents;

public class SwaggerPdfContents {

    public static final String UPLOAD_RESPONSE = "{\n  \"isSuccess\": true,\n  \"code\": \"COMMON200\",\n  \"message\": \"요청에 성공하였습니다.\",\n  \"result\": {\n    \"id\": 1,\n    \"originalName\": \"이력서.pdf\",\n    \"pdfName\": \"이력서.pdf\",\n    \"pdfPath\": \"/pdfs/some-uuid-이력서.pdf\",\n    \"pdfUrl\": \"http://localhost:8080/pdfs/some-uuid-이력서.pdf\",\n    \"pdfSize\": 123456\n  }\n}";

    public static final String GET_RESPONSE = "{\n  \"isSuccess\": true,\n  \"code\": \"COMMON200\",\n  \"message\": \"요청에 성공하였습니다.\",\n  \"result\": {\n    \"id\": 1,\n    \"originalFilename\": \"이력서.pdf\",\n    \"savedPath\": \"/pdfs/some-uuid-이력서.pdf\",\n    \"contentType\": \"application/pdf\",\n    \"fileSize\": 123456,\n    \"resumeId\": 1\n  }\n}";

    public static final String DELETE_RESPONSE = "{\n  \"isSuccess\": true,\n  \"code\": \"COMMON200\",\n  \"message\": \"요청에 성공하였습니다.\",\n  \"result\": null\n}";
}
