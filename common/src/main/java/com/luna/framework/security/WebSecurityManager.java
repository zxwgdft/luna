package com.luna.framework.security;

import com.luna.framework.api.AuthenticationToken;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WebSecurityManager extends SecurityManager implements HandlerInterceptor {

    @Value("${auth.token-field:Authorization}")
    private String authTokenField;
    @Value("${auth.token-refresh-field:New-Token}")
    private String refreshTokenField;
    @Value("${auth.token-refresh-interval-milliseconds:1800000}")
    private long tokenRefreshIntervalMilliseconds;
    @Value("${auth.token-use-cookie:false}")
    private boolean tokenUseCookie;

    // 当前请求的线程变量
    private final static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    // 登录页面
    private String loginUrl = null;
    private List<String> ignorePrefixes = new ArrayList<>(2);
    private Class<? extends UserClaims> userClazz;

    public WebSecurityManager(Class<? extends UserClaims> userClazz) {
        this.userClazz = userClazz;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 注入请求到线程变量（为了性能把请求绑定操作放在这，如果不爽可以单独拎出一个HandlerInterceptor，并注册到spring）
        requestThreadLocal.set(request);

        if (isIgnored(request)) {
            return true;
        }

        // 安全认真部分，获取token并验证
        // 先尝试在header获取，没有则再尝试从cookie中获取
        String token = request.getHeader(authTokenField);
        boolean isCookie = false;
        if (tokenUseCookie && StringUtil.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (StringUtil.equals(cookie.getName(), authTokenField)) {
                        token = cookie.getValue();
                        isCookie = true;
                        break;
                    }
                }
            }
        }

        if (StringUtil.isNotEmpty(token)) {
            // 校验token
            UserClaims userClaims;
            try {
                userClaims = tokenProvider.parseJWT(token, userClazz);
                long interval = userClaims.getExp() - System.currentTimeMillis();
                if (interval < 0) {
                    // token 过期注销
                    logout(request, response);
                } else {
                    UserSession userSession = createUserSession(userClaims);
                    if (userSession != null) {
                        if (tokenRefreshIntervalMilliseconds > 0) {
                            // 检查token是否将要过期，是则刷新一个token
                            if (interval < tokenRefreshIntervalMilliseconds) {
                                userClaims.setExp(-1);
                                String newToken = createToken(userClaims);
                                if (isCookie) {
                                    response.addCookie(getAuthCookie(newToken));
                                }
                                response.addHeader(refreshTokenField, newToken);
                            }
                        }
                        sessionMap.set(userSession);
                        return true;
                    }
                }
            } catch (Exception e) {
                log.debug("认证校验未通过", e);
                // do nothing
            }
        }

        sessionMap.set(null);

        // 判断是否登录
        if (StringUtil.equals(loginUrl, request.getRequestURI())) {
            return true;
        }

        // ajax 请求按照异常处理，并不需要跳转页面
        throw new BusinessException(HttpStatus.UNAUTHORIZED, "未认证，请先登录");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        sessionMap.remove();
        requestThreadLocal.remove();
    }

    /**
     * web下认证调用
     */
    public String authorize(AuthenticationToken token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        requestThreadLocal.set(request);

        String jwtToken = authorize(token);
        if (tokenUseCookie) {
            response.addCookie(getAuthCookie(jwtToken));
        }
        return jwtToken;
    }


    /**
     * web下注销
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if (tokenUseCookie) {
            Cookie cookie = getAuthCookie(null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    protected Cookie getAuthCookie(String value) {
        Cookie cookie = new Cookie(authTokenField, value);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * 是否忽略对请求的安全处理
     * <p>
     * 如果不想通过spring mvc的PathMatcher + PathPatterns处理哪些请求需要处理，
     * 可设置ignorePrefix值对匹配的前缀请求进行忽略，或重写以下方法实现自己的逻辑。
     * <p>
     * 注：可以接口形式提供该方法，从而使之更灵活
     */
    public boolean isIgnored(HttpServletRequest request) {
        if (ignorePrefixes.size() > 0) {
            String uri = request.getRequestURI();
            for (String ignorePrefix : ignorePrefixes) {
                if (uri.startsWith(ignorePrefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void addIgnorePrefix(String ignorePrefix) {
        this.ignorePrefixes.add(ignorePrefix);
    }

    /**
     * 获取当前线程的web请求
     */
    public static HttpServletRequest getCurrentRequest() {
        return requestThreadLocal.get();
    }
}
