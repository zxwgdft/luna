package com.luna.tenant.config;

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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Configuration
public class TenantWebConfigurer implements WebMvcConfigurer {

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
        registry.addInterceptor(webSecurityManager).addPathPatterns("/tenant/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter());
    }

    @Value("${auth.token-refresh-field:New-Token}")
    private String refreshTokenField;

    // 需要跨域修改该代码并注入spring
    @Bean
    @ConditionalOnProperty(prefix = "tenant", value = "cors-enabled", havingValue = "true", matchIfMissing = false)
    public FilterRegistrationBean<CorsFilter> registerCorsFilter() {
        // 对响应头进行CORS授权
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
        corsConfiguration.setMaxAge(3600L);// 跨域过期时间 秒


        // 注意：如果使用网关等其他方式跨域，需要加入以下头允许
        corsConfiguration.setExposedHeaders(Arrays.asList(refreshTokenField));

        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
