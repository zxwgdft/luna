package com.luna.his.config;

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
                .apis(RequestHandlerSelectors.basePackage("com.luna.his.controller.core"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("非业务类核心接口");
    }

    @Bean
    public Docket createInternalDocket() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.luna.his.controller.internal"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("内部服务接口");
    }

    @Bean
    public Docket createOrgDocket() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.luna.his.controller.org"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("组织架构接口");
    }

    @Bean
    public Docket createPatientDocket() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.luna.his.controller.patient"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("患者相关接口");
    }

    @Bean
    public Docket createSysDocket() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.luna.his.controller.sys"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("系统配置接口");
    }
}
