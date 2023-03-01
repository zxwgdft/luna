package com.luna.framework.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author TontoZhou
 */

@Data
@ConfigurationProperties(prefix = "web.security")
public class WebSecurityProperties {

    // 是否启用cookie存取token
    private boolean tokenUseCookie = false;
    // 用户声明信息对应类
    private Class<? extends UserClaims> userClaimsClass = UserClaims.class;
    // 登录请求
    private String loginUrl = null;
    // 忽略的请求前缀
    private List<String> ignorePrefixes = null;
    // 认证token对应的字段（header）
    private String authTokenField = "Authorization";
    // 刷新token对应的字段（header）
    private String refreshTokenField = "New-Token";
    // token刷新间隔（毫秒）
    private long tokenRefreshIntervalMilliseconds = 1800000;
    // token过期时间（毫秒），默认一小时
    private int tokenExpireMilliseconds = 3600000;
    // token长过期时间（毫秒），默认一天
    private int tokenLongExpireMilliseconds = 86400000;
    // RSA私钥（用于jwt）
    private String rsaPrivateKey;
    // RSA公钥（用于jwt）
    private String rsaPublicKey;

}
