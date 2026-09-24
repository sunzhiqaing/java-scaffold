package com.opc.scaffold.common.exception;

import lombok.Getter;

/**
 * 业务异常：Service 层抛出的可预期业务错误
 */
@Getter
public class BizException extends RuntimeException {

    private final Integer code;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message) {
        super(message);
        this.code = 400;
    }
}
