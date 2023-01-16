package com.luna.his;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author TontoZhou
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@MapperScan({
        "com.luna.his.*.mapper",
})
@EnableFeignClients({
        "com.luna.tenant.client",
})
@EnableSwagger2
public class HisMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(HisMainApplication.class, args);
    }

}
