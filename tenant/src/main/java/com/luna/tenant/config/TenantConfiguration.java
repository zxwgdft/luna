package com.luna.tenant.config;

import com.luna.framework.security.*;
import com.luna.tenant.core.TenantAuthorizer;
import com.luna.tenant.core.TenantUserClaims;
import com.luna.tenant.core.TenantUserSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WebSecurityProperties.class)
public class TenantConfiguration {

    @Bean
    public WebSecurityManager getSecurityManager(WebSecurityProperties properties) {
        return new WebSecurityManager(properties);
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
