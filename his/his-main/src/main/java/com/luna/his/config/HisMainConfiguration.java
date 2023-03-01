package com.luna.his.config;

import com.luna.framework.security.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.luna.his.core.HisAuthorizer;
import com.luna.his.core.HisUserClaims;
import com.luna.his.core.HisUserSessionFactory;

@Configuration
@EnableConfigurationProperties(WebSecurityProperties.class)
public class HisMainConfiguration {

    @Bean
    public WebSecurityManager getSecurityManager(WebSecurityProperties properties) {
        return new WebSecurityManager(properties);
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
