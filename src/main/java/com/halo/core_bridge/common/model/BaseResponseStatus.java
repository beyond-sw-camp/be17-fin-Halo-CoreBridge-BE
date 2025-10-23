package com.halo.core_bridge.common.model;


import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 20000 : 요청 성공
     */
    SUCCESS(true, 20000, "요청에 성공하였습니다."),


    /**
     * 30000 : Request 오류, Validation 오류
     */
    // Common
    FIELD_VALIDATE_ERROR(false, 20001, "입력값 예외가 발생했습니다. 올바른 값을 입력하세요."),
    INVALID_JWT(false, 20002, "유효하지 않은 JWT입니다."),
    INVALID_USER_ROLE(false,20003,"권한이 없는 유저의 접근입니다."),
    INVALID_USER_INFO(false,20004,"이메일 또는 비밀번호를 확인해주세요."),
    INVALID_USER_DISABLED(false,20005,"이메일 인증이 필요합니다. 이메일을 확인해주세요."),
    DUPLICATE_USER_EMAIL(false,20006,"중복된 이메일입니다. 다른 이메일을 사용해주세요."),
    NOT_FOUND_USER(false, 20007, "존재하지 않는 사용자입니다."),

    GLOBAL_EXCEPTION(false, 30000, "요청을 처리하는 과정에서 문제가 발생하였습니다."),
    REQUEST_ERROR(false, 30001, "입력값을 확인해주세요."),
    INVALID_AUTH_CODE(false, 30002, "유효하지 않은 인증번호 입니다."),
    EXPIRED_JWT(false, 30008, "JWT 토큰이 만료되었습니다."),
    JSON_PARSER(false, 30009 , "JSON Parsing 실패"),
    INVALID_REFRESH_TOKEN(false, 30010, "유효하지 않는 토큰입니다."),
    EXCEPTION_CREATE_ACCESS_TOKEN(false, 30011, "토큰 생성 중 예기지 못한 오류가 발생하였습니다."),

    FILE_UPLOAD_ERROR(false, 30012, "파일 업로드에 실패했습니다"),

    // 게시판
    NOT_FOUNT_BOARD(false, 31000, "해당 게시글이 존재하지 않습니다."),

    /**
     * 40000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 40001, "값을 불러오는데 실패하였습니다."),
    RESUME_NOT_FOUND(false, 40002, "이력서를 찾을 수 없습니다."),
    FAILD_FOUND_USER(false, 40003, "사용자를 찾을 수 없습니다."),
    FAILD_FOUND_JOBPOST(false, 40003, "공고를 찾을 수 없습니다."),

    /**
     * 50000 : Database 오류
     */
    DATABASE_ERROR(false, 50001, "데이터베이스 연결에 실패하였습니다."),

    /**
     * 60000 : Server 오류
     */
    SERVER_ERROR(false, 60001, "서버와의 연결에 실패하였습니다."),


    /**
     * 70000 : 커스텀
     */
    JOB_POSTING_NOT_FOUND(false, 70001, "존재하지 않는 채용공고입니다."),
    JOB_POSTING_EMPTY(false, 70002, "등록된 채용공고가 없습니다."),
    DEPARTMENT_NOT_FOUND(false, 70003, "존재하지 않는 부서 정보입니다."),
    IMAGE_NOT_FOUND(false, 70004, "이미지를 찾을 수 없습니다."),
    INVALID_IMAGE_FILE(false, 70005, "유효하지 않은 이미지 파일입니다."),
    IMAGE_UPLOAD_FAILED(false, 70006, "이미지 업로드에 실패했습니다."),
    INVALID_PDF_FILE(false, 70007, "유효하지 않은 PDF파일 입니다"),
    PDF_UPLOAD_FAILED(false, 70008, "PDF 업로드에 실패했습니다."),
    PDF_NOT_FOUND(false, 70009, "PDF를 찾을 수 없습니다."),
    UNSUPPORTED_FILE_TYPE(false, 70009, "지원하지 않는 형식입니다."),
    FILE_TOO_LARGE(false, 700010, "용량을 초과하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
