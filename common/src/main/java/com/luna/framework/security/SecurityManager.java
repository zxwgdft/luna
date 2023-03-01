package com.luna.framework.security;

import com.luna.framework.api.AuthenticationToken;
import com.luna.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
public abstract class SecurityManager {

    @Autowired(required = false)
    protected List<Authorizer> authorizers;
    @Autowired(required = false)
    protected List<AuthenticationListener> authenticationListeners;
    @Autowired(required = false)
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
     * <p>
     * 如果不想通过UserSessionFactory接口实现，可不注入并重写该方法
     */
    public UserSession createUserSession(UserClaims userClaims) {
        return userSessionFactory.createUserSession(userClaims);
    }

    /**
     * 生成token
     *
     * @param userClaims 用户声明
     */
    public abstract String createToken(UserClaims userClaims);

    /**
     * 设置token的过期时间
     *
     * @param userClaims
     */
    public void setTokenExp(UserClaims userClaims, AuthenticationToken token) {

    }

    /**
     * 认证并返回token
     */
    public String authorize(AuthenticationToken token) throws Exception {
        return authorize(token, authorizers, authenticationListeners);
    }

    /**
     * 认证并返回token
     */
    public String authorize(AuthenticationToken token, List<Authorizer> authorizers,
                            List<AuthenticationListener> authenticationListeners) throws Exception {
        // 只需要认证成功一个则算登录成功
        boolean success = false;
        UserSession userSession = null;
        Exception exception = null;
        String jwtToken = null;

        if (authorizers != null && authorizers.size() > 0) {
            try {
                for (Authorizer authorizer : authorizers) {
                    UserClaims userClaims = authorizer.authorize(token);
                    if (userClaims != null) {
                        userSession = createUserSession(userClaims);
                        setTokenExp(userClaims, token);
                        sessionMap.set(userSession);
                        jwtToken = createToken(userClaims);
                        success = true;
                        break;
                    }
                }
            } catch (Exception e) {
                exception = e;
            }
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