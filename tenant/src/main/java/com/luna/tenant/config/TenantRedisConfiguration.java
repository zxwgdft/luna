package com.luna.tenant.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author TontoZhou
 * @since 2018年3月15日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class TenantRedisConfiguration extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheManager redisCacheManager = RedisCacheManager.create(lettuceConnectionFactory);
        return redisCacheManager;
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> getJsonRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        return redisTemplate;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate getStringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }
}
