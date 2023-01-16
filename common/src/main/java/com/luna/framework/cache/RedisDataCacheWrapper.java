package com.luna.framework.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于redis乐观锁实现的数据缓存
 */
@Slf4j
public class RedisDataCacheWrapper<T> implements DataCache<T> {

    private RedisTemplate<String, String> redisTemplate;
    private DataCache<T> source;
    private volatile long version = -1;
    private String cacheKey;
    private T data;

    public RedisDataCacheWrapper(DataCache<T> dataCache, RedisTemplate<String, String> redisTemplate, String cacheKey) {
        this.cacheKey = cacheKey;
        this.source = dataCache;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void dataChanged() {
        try {
            redisTemplate.opsForValue().increment(cacheKey);
        } catch (Exception e) {
            log.error("数据缓存异常！", e);
            synchronized (source) {
                version = -1;
            }
        }
    }

    @Override
    public T loadData() {
        return source.loadData();
    }

    @Override
    public T getData() {
        try {
            long current = getVersion();
            if (current != version) {
                synchronized (source) {
                    // 是否要二次获取判断，还是直接更新，
                    // 如果数据变更几率低，并且加载数据越复杂，则前者更有利
                    current = getVersion();
                    if (version != current) {
                        if (current > version) {
                            data = source.loadData();
                            version = current;
                        } else {
                            data = source.loadData();
                            redisTemplate.opsForValue().set(cacheKey, String.valueOf(version));
                        }
                    }
                }
            }
            return data;
        } catch (Exception e) {
            log.error("数据缓存异常！", e);
            return source.loadData();
        }
    }

    private long getVersion() {
        String value = redisTemplate.opsForValue().get(cacheKey);
        return value == null ? 0 : Long.valueOf(value);
    }

}
