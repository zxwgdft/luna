package com.luna.framework.security;

/**
 * @author TontoZhou
 * @since 2019/12/26
 */
public interface TokenProvider {
    /**
     * 创建JWT
     *
     * @param userClaims 用户声明信息
     */
    String createJWT(UserClaims userClaims);

    /**
     * 解析JWT，获取相关信息
     *
     * @param jwtToken
     * @return
     */
    UserClaims parseJWT(String jwtToken, Class<? extends UserClaims> clazz);

}
