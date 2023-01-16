package com.luna.his.config;

import com.google.common.collect.Lists;
import com.luna.framework.spring.NotProduceCondition;
import com.luna.framework.spring.SpringBeanHelper;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/12/30
 */
@Configuration
@Conditional(NotProduceCondition.class)
@EnableOpenApi
public class SwaggerConfiguration {

    @PostConstruct
    public void createRestApi() {
        createDocket();
    }

    public void createDocket() {
        List<Response> responseMessageList = getGlobalResponses();
        // 循环SwaggerEntity中的所有定义
        for (SwaggerGroup value : SwaggerGroup.values()) {
            // 注册bean
            Docket docket = SpringBeanHelper.registerBean(null, Docket.class, Lists.newArrayList(DocumentationType.OAS_30), null);
            docket.pathMapping("/")
                    .apiInfo(apiInfo())
                    .select()
                    .apis(value.getApis())
                    .paths(value.getPaths())
                    .build()
                    .groupName(value.getGroupName())
                    .useDefaultResponseMessages(false)
                    .globalResponses(HttpMethod.GET, responseMessageList)
                    .globalResponses(HttpMethod.POST, responseMessageList);
        }
    }

    public List<Response> getGlobalResponses() {
        List<Response> responseMessageList = new ArrayList<>();

        ObjectVendorExtension errorResponse = new ObjectVendorExtension("ErrorResponse");
        errorResponse.addProperty(new StringVendorExtension("code", "错误编码"));
        errorResponse.addProperty(new StringVendorExtension("message", "错误信息"));
        errorResponse.addProperty(new StringVendorExtension("data", "错误相关数据"));

        List<VendorExtension> extensions = new ArrayList<>();
        extensions.add(errorResponse);

        responseMessageList.add(new ResponseBuilder().code("400").description("请求异常").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseBuilder().code("404").description("未找到请求资源").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseBuilder().code("401").description("请求身份未验证").vendorExtensions(extensions).build());
        responseMessageList.add(new ResponseBuilder().code("500").description("操作失败").vendorExtensions(extensions).build());

        return responseMessageList;
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SERVICE API")
                .termsOfServiceUrl("")
                .contact(new Contact("TontoZhou", "https://github.com/zxwgdft", "823498927@qq.com"))
                .version("1.0")
                .build();
    }

}
