package com.luna.his.core.cache;

import com.luna.framework.cache.DataCache;
import com.luna.framework.cache.RedisDataCacheWrapper;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于租户的数据缓存基本模型
 * <p>
 *
 * @author TontoZhou
 * @since 2022/11/19
 */
public class TenantDataCache<T> {

    // 租户缓存用于redis的key前缀
    private static final String keyPrefix = "_TDC_";

    private Map<Long, DataCache<T>> tenantCacheMap = new ConcurrentHashMap<>();

    private RedisTemplate<String, String> redisTemplate;

    private TenantDataCacheFactory<T> cacheFactory;

    public TenantDataCache(TenantDataCacheFactory<T> cacheFactory, RedisTemplate<String, String> redisTemplate) {
        if (cacheFactory == null || redisTemplate == null) {
            throw new NullPointerException();
        }
        this.cacheFactory = cacheFactory;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 当数据变化时应当触发该方法
     *
     * @Param tenantId 租户ID
     */
    public void dataChanged(long tenantId) {
        DataCache dataCache = tenantCacheMap.get(tenantId);
        if (dataCache != null) {
            dataCache.dataChanged();
        }
    }

    /**
     * 加载数据
     *
     * @Param tenantId 租户ID
     */
    public T loadData(long tenantId) {
        DataCache<T> dataCache = _getDataCache(tenantId);
        return dataCache.loadData();
    }

    /**
     * 获取缓存的数据
     *
     * @Param tenantId 租户ID
     */
    public T getData(long tenantId) {
        DataCache<T> dataCache = _getDataCache(tenantId);
        return dataCache.getData();
    }

    /**
     * 获取租户级别的数据缓存
     */
    private DataCache<T> _getDataCache(long tenantId) {
        DataCache<T> dataCache = tenantCacheMap.get(tenantId);
        if (dataCache == null) {
            synchronized (tenantCacheMap) {
                dataCache = tenantCacheMap.get(tenantId);
                if (dataCache == null) {
                    dataCache = cacheFactory.createDataCache(tenantId);
                    String cacheKey = keyPrefix + cacheFactory.getCacheId() + "_" + tenantId;
                    dataCache = new RedisDataCacheWrapper(dataCache, redisTemplate, cacheKey);
                    tenantCacheMap.put(tenantId, dataCache);
                }
            }
        }
        return dataCache;
    }


}
