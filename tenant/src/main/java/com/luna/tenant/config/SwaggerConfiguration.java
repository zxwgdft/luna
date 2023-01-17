package com.luna.tenant.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author TontoZhou
 * @since 2019/12/30
 */
@Configuration
@ConditionalOnProperty(prefix = "swagger", value = "enabled", havingValue = "true", matchIfMissing = false)
@EnableOpenApi
public class SwaggerConfiguration {

    private final static ApiInfo apiInfo;

    static {
        apiInfo = new ApiInfoBuilder()
                .title("SERVICE API")
                .termsOfServiceUrl("")
                .contact(new Contact("TontoZhou", "https://github.com/zxwgdft", "823498927@qq.com"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createCoreDocket() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.luna.tenant.controller"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

}
