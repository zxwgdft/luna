package com.luna.tenant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 动态调用his相关服务
 *
 * @author TontoZhou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicHisServlet {

    private final RestTemplate restTemplate;

    /**
     * 发送json数据格式的请求
     *
     * @param appName 应用名称
     * @param path    请求地址
     * @param param   请求参数
     * @param clazz   返回数据类型
     * @param <Req>   请求参数泛型
     * @param <Res>   返回参数泛型
     * @return
     */
    public <Req, Res> Res postJsonRequest(String appName, String path, Req param, Class<Res> clazz) {
        String url = "http://his-main-" + appName + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Req> entity = new HttpEntity<>(param, headers);
        return restTemplate.postForEntity(url, entity, clazz).getBody();
    }

}
