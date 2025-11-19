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
    PROCESS_NOT_FOUND(false, 40003, "이력서를 찾을 수 없습니다."),
    FAILD_FOUND_USER(false, 40004, "사용자를 찾을 수 없습니다."),
    FAILD_FOUND_JOBPOST(false, 40005, "공고를 찾을 수 없습니다."),
    FAILD_FOUND_COVERLETTER(false, 40006, "답변을 찾을 수 없습니다."),
    FAILD_FOUND_COVERLETTER_TITLE(false, 40007, "질문을 찾을 수 없습니다."),


    /**
     * 50000 : Database 오류
     */
    DATABASE_ERROR(false, 50001, "데이터베이스 연결에 실패하였습니다."),
    FAILED_DELETE_DATA(false, 50002, "연관된 데이터가 있어 유저를 삭제 할 수 없습니다."),

    /**
     * 60000 : Server 오류
     */
    SERVER_ERROR(false, 60001, "서버와의 연결에 실패하였습니다."),
    GLOBAL_ERROR(false, 60002, "올바르지 않은 요청입니다. 로그를 확인해주세요."),


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
    FILE_TOO_LARGE(false, 700010, "용량을 초과하였습니다."),
    DELETE_NOT_ALLOWED_DURING_APPLICATION(false, 700011, "접수 기간 중에는 채용공고를 삭제할 수 없습니다."),

    JOB_POSTING_SCHEDULE_NOT_FOUND(false, 71001, "존재하지 않는 공고 일정입니다."),
    PROCESS_SCHEDULE_NOT_FOUND(false, 72001, "존재하지 않는 프로세스 일정입니다."),
    SCHEDULE_SHARE_NOT_FOUND(false, 73001, "존재하지 않는 공유 정보입니다."),

    RECRUIT_PROCESS_NOT_FOUND(false, 74001, "해당 공고에는 채용 프로세스가 존재하지 않습니다."),
    RECRUIT_PROCESS_CANT_DELETE(false, 74002, "채용 프로세스에 지원자가 존재하면 삭제 할 수 없습니다."),

    // 면접
    DUPLICATE_INTERVIEW_ROOM(false, 75000, "면접 장소는 중복 될 수 없습니다."),
    NOT_FOUND_INTERVIEW_ROOM(false, 75001, "면접 장소를 찾을 수 없습니다."),
    NOT_PROVIDED_CAPACITY_FOR_OFFLINE_ROOM(false, 75002, "오프라인 장소에는 수용인원이 필요합니다."),
    ALREADY_SCHEDULED_INTERVIEW(false, 75003, "해당 지원자는 이미 면접을 예약하였습니다"),
    RESUME_PROCESS_NOT_MATCH(false, 75004, "이력서와 채용 프로세스가 일치하지 않습니다"),
    INTERVIEW_NOT_FOUND(false, 75005, "존재하지 않는 면접입니다."),
    CANNOT_CANCEL_INTERVIEW(false, 75006, "취소 할 수 없는 면접입니다."),

    // 평가
    NOT_FOUND_ASSIGNMENT(false, 76000, "존재하지 않는 면접관입니다."),
    CANNOT_MODIFY_EVALUATION(false, 76001, "평가는 수정하지 못합니다."),
    NOT_FOUND_INTERVIEW_ASSIGNMENT(false, 76002, "존재하지 않는 면접관입니다"),

    // 사용자
    NOT_FOUND_USER_ROLE(false, 75002, "권한을 찾을 수 없습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
