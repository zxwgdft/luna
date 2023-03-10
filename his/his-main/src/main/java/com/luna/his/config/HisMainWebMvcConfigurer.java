package com.luna.his.config;


import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.web.convert.DateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
public class HisMainWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private WebSecurityManager webSecurityManager;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webSecurityManager).addPathPatterns("/his/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter());
    }

    @Value("${auth.token-refresh-field:New-Token}")
    private String refreshTokenField;

    // ????????????????????????????????????spring
    @Bean
    @ConditionalOnProperty(prefix = "his", value = "cors-enabled", havingValue = "true", matchIfMissing = false)
    public FilterRegistrationBean<CorsFilter> registerCorsFilter() {
        // ??????????????????CORS??????
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1????????????????????????
        corsConfiguration.addAllowedHeader("*"); // 2???????????????
        corsConfiguration.addAllowedMethod("*"); // 3?????????????????????post???get??????
        corsConfiguration.setMaxAge(3600L);// ?????????????????? ???


        // ??????????????????????????????????????????????????????????????????????????????
        corsConfiguration.setExposedHeaders(Arrays.asList(refreshTokenField));

        // ??????CORS?????????
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
