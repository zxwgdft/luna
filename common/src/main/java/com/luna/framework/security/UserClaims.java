package com.luna.framework.security;


import lombok.Data;

/**
 * @author TontoZhou
 * @since 2020/8/27
 */
@Data
public abstract class UserClaims {
    /**
     * 用户过期时间
     */
    private long exp;
}
