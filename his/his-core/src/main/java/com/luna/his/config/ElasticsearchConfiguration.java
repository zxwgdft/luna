package com.luna.his.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author TontoZhou
 */
@Slf4j
@Configuration
public class ElasticsearchConfiguration {

    @Bean
    public RestClient getRestClient(Environment env) {
        String host = env.getProperty("elasticsearch.host");
        String port = env.getProperty("elasticsearch.port");
        RestClient restClient = RestClient.builder(
                new HttpHost(host, Integer.parseInt(port))
        ).build();
        return restClient;
    }

    @Bean
    public ElasticsearchTransport getTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }


}
