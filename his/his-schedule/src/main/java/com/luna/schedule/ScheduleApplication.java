package com.luna.schedule;

/**
 * @author TontoZhou
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableConfigurationProperties
@SpringBootApplication(
        scanBasePackages = {
                "com.luna.schedule",
                "com.luna.his"
        })
@MapperScan({
        "com.luna.his.*.mapper",
})
@EnableFeignClients({
        "com.luna.tenant.client",
})
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ScheduleApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

}