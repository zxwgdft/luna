package com.luna.framework.security;

import com.luna.framework.api.AuthenticationToken;
import com.luna.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
public abstract class SecurityManager {

    @Value("${auth.token-expire-milliseconds:3600000}")
    protected int tokenExpireMilliseconds;

    @Autowired
    protected TokenProvider tokenProvider;
    @Autowired
    protected List<Authorizer> authorizers;
    @Autowired(required = false)
    protected List<AuthenticationListener> authenticationListeners;

    @Autowired
    protected UserSessionFactory userSessionFactory;

    // 用户信息的线程变量
    protected final static ThreadLocal<UserSession> sessionMap = new ThreadLocal<>();

    /**
     * 需要保证调用前已经绑定了UserSession
     *
     * @return 当前线程用户会话
     */
    public static UserSession getCurrentUserSession() {
        return sessionMap.get();
    }


    /**
     * 创建用户会话
     */
    protected UserSession createUserSession(UserClaims userClaims) {
        return userSessionFactory.createUserSession(userClaims);
    }

    /**
     * 创建新的token
     *
     * @param userClaims 用户声明
     */
    public String createToken(UserClaims userClaims) {
        long current = System.currentTimeMillis();
        if (userClaims.getExp() < current) {
            userClaims.setExp(current + tokenExpireMilliseconds);
        }
        return tokenProvider.createJWT(userClaims);
    }

    /**
     * 认证
     */
    public String authorize(AuthenticationToken token) throws Exception {
        // 只需要认证成功一个则算登录成功
        boolean success = false;
        UserSession userSession = null;
        Exception exception = null;
        String jwtToken = null;

        try {
            for (Authorizer authorizer : authorizers) {
                UserClaims userClaims = authorizer.authorize(token);
                if (userClaims != null) {
                    userSession = createUserSession(userClaims);
                    sessionMap.set(userSession);
                    jwtToken = createToken(userClaims);
                    success = true;
                    break;
                }
            }
        } catch (Exception e) {
            exception = e;
        }

        if (authenticationListeners != null && authenticationListeners.size() > 0) {
            for (AuthenticationListener listener : authenticationListeners) {
                if (success) {
                    listener.onSuccess(token, userSession);
                } else {
                    listener.onFailure(token, exception);
                }
            }
        }

        if (exception != null) {
            throw exception;
        }

        if (success) {
            return jwtToken;
        }

        throw new BusinessException("登录失败！");
    }


}