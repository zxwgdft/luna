package com.luna.tenant.config;

import com.luna.framework.security.*;
import com.luna.tenant.core.TenantAuthorizer;
import com.luna.tenant.core.TenantUserClaims;
import com.luna.tenant.core.TenantUserSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Slf4j
@Configuration
public class TenantConfiguration {

    /**
     * JWT提供器
     */
    @Bean
    public TokenProvider getTokenProvider(Environment environment) throws Exception {
        String privateKeyString = environment.getProperty("auth.rsa-private-key");
        String publicKeyString = environment.getProperty("auth.rsa-public-key");
        return new RSATokenProvider(privateKeyString, publicKeyString);
    }

    @Bean
    public WebSecurityManager getSecurityManager(Environment environment) {
        WebSecurityManager securityManager = new WebSecurityManager(TenantUserClaims.class);
        securityManager.setLoginUrl("/tenant/login");
        // 忽略以下前缀请求
        //securityManager.addIgnorePrefix("/his/open/");
        return securityManager;
    }

    @Bean
    public UserSessionFactory getUserSessionFactory() {
        return new TenantUserSessionFactory();
    }

    @Bean
    public Authorizer getAuthorizer() {
        return new TenantAuthorizer();
    }

}
