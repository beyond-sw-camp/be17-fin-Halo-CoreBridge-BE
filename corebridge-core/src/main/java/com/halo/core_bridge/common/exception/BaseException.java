package com.halo.core_bridge.common.exception;

import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final BaseResponseStatus status;

    public BaseException(String message, BaseResponseStatus status) {
        super(message);
        this.status = status;
    }

    public static BaseException from(BaseResponseStatus status) {
        return new BaseException(status.getMessage(), status);
    }

}
