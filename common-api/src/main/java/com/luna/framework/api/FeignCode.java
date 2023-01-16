package com.luna.framework.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Getter
@AllArgsConstructor
public class FeignCode implements IResultCode {
    private int code;
    private String message;
}
