package com.halo.core_bridge.common.exception;

import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private int httpStatusCodeMapper(int statusCode) {

        if (31001 <= statusCode && statusCode < 32000)
            return 200;

        if (statusCode >= 40000) {
            return 500;
        }

        return 400;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Object>> handleException(BaseException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(httpStatusCodeMapper(e.getStatus().getCode()))
                .body(
                        BaseResponse.error(e.getStatus())
                );
    }

    // @Valid 검증 실패 처리
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(httpStatusCodeMapper(e.getStatusCode().value()))
                .body(
                        BaseResponse.error(BaseResponseStatus.FIELD_VALIDATE_ERROR, errors)
                );
    }
}
