package com.halo.core_bridge.common.model;


import lombok.Getter;
import lombok.Setter;

import static com.halo.core_bridge.common.model.BaseResponseStatus.*;


@Getter
@Setter
public class BaseResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T results;

    public BaseResponse(boolean success, int code, String message, T results) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.results = results;
    }

    public static <T> BaseResponse<T> success(T results) {
        return new BaseResponse<>(SUCCESS.isSuccess(), SUCCESS.getCode(), SUCCESS.getMessage(), results);
    }

    public static <T> BaseResponse<T> error(BaseResponseStatus status) {
        return new BaseResponse<>(status.isSuccess(), status.getCode(), status.getMessage(), null);
    }

    public static <T> BaseResponse<T> error(BaseResponseStatus status, T results) {
        return new BaseResponse<>(status.isSuccess(), status.getCode(), status.getMessage(), results);
    }
}
