package com.luna.framework.exception;

import org.springframework.http.HttpStatus;

/**
 * 非法的请求异常
 * <p>
 * 例如请求参数可能为非正常流程获取，可能为恶意请求获取他人数据所用。
 */
public class IllegalRequestException extends BusinessException {

    public IllegalRequestException() {
        super(HttpStatus.METHOD_NOT_ALLOWED, "非法的请求");
    }
}
