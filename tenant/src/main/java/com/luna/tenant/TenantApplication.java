package com.luna.tenant;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author TontoZhou
 * @since 2019/10/30
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({
        "com.luna.support.client",
})
@MapperScan(basePackages = "com.luna.tenant.mapper")
public class TenantApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantApplication.class, args);
    }

}
