package com.luna.framework.spring;

import com.luna.framework.cache.DataCache;
import com.luna.framework.cache.RedisDataCacheWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisDataCacheFactory extends DataCacheFactory {

    private String keyPrefix = "_DCV_";
    private RedisTemplate<String, String> redisTemplate;

    public RedisDataCacheFactory(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisDataCacheFactory(String keyPrefix, RedisTemplate<String, String> redisTemplate) {
        this.keyPrefix = keyPrefix;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public DataCache wrapDataCache(DataCache dataCache) {
        return new RedisDataCacheWrapper(dataCache, redisTemplate, keyPrefix + dataCache.getId());
    }
}
