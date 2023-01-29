package com.luna.his.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.luna.framework.security.Authorizer;
import com.luna.framework.security.RSATokenProvider;
import com.luna.framework.security.TokenProvider;
import com.luna.framework.security.UserSessionFactory;
import com.luna.framework.security.WebSecurityManager;
import com.luna.his.core.HisAuthorizer;
import com.luna.his.core.HisUserClaims;
import com.luna.his.core.HisUserSessionFactory;

@Configuration
public class HisMainConfiguration {

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
        WebSecurityManager securityManager = new WebSecurityManager(HisUserClaims.class);
        securityManager.setLoginUrl("/his/login");
        // 忽略以下前缀请求
        //securityManager.addIgnorePrefix("/his/open/");
        return securityManager;
    }

    @Bean
    public UserSessionFactory getUserSessionFactory() {
        return new HisUserSessionFactory();
    }

    @Bean
    public Authorizer getAuthorizer() {
        return new HisAuthorizer();
    }

}
