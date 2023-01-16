package com.luna.framework.security;

import com.luna.framework.api.AuthenticationToken;

public interface Authorizer {

    /**
     * 用户认证
     * <p>
     * 如果认证失败，并且明确不应该继续认证下去，则应该抛出异常
     *
     * @param token 用户认证token
     * @return 返回null表示可以继续下一个认证器认证
     */
    UserClaims authorize(AuthenticationToken token);


}
