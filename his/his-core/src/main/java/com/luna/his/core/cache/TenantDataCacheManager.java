package com.luna.his.core.cache;

import com.luna.framework.api.SystemException;
import com.luna.framework.spring.SpringBeanHelper;
import com.luna.framework.utils.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 适合系统内部常用的数据缓存，例如常量、字典、标准等数据。
 * <p>
 * 基于租户的版本
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
@Slf4j
public class TenantDataCacheManager implements ApplicationRunner {

    private RedisTemplate<String, String> redisTemplate;

    public TenantDataCacheManager(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private Map<String, TenantDataCache> id2CacheMap = new ConcurrentHashMap<>();
    private Map<String, TenantDataCache> dataClass2CacheMap = new ConcurrentHashMap<>();

    /**
     * 注册数据缓存对象
     */
    public <T> void register(TenantDataCache<T> dataCache, Class<T> dataClass, String cacheId) {
        if (id2CacheMap.containsKey(cacheId)) {
            log.error("存在多个DataCache的ID为[" + cacheId + "]，重复DataCache将会被覆盖");
        }
        id2CacheMap.put(cacheId, dataCache);

        if (dataClass == null) {
            log.debug("DataCache的dataClass不能为null");
        } else {
            String dataClassName = dataClass.getName();
            if (dataClass2CacheMap.containsKey(dataClassName)) {
                log.debug("存在多个DataCache的数据缓存对象为[class={}]，通过数据类型获取缓存时可能会出错");
            }
            dataClass2CacheMap.put(dataClassName, dataCache);
        }
    }

    /**
     * 通过数据类型获取数据缓存对象
     */
    public <T> TenantDataCache<T> getDataCache(Class<T> dataClazz) {
        TenantDataCache dataCache = dataClass2CacheMap.get(dataClazz.getName());
        if (dataCache == null) {
            log.error("不存在的数据缓存[dataClass={}]", dataClazz);
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
        return dataCache;
    }

    /**
     * 通过ID获取数据缓存对象
     */
    public TenantDataCache getDataCache(String cacheId) {
        TenantDataCache dataCache = id2CacheMap.get(cacheId);
        if (dataCache == null) {
            log.error("不存在的数据缓存[cacheId={}]", cacheId);
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
        return dataCache;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("扫描数据缓存对象...");

        Map<String, TenantDataCacheFactory> dataCacheBeanMap = SpringBeanHelper.getBeansByType(TenantDataCacheFactory.class);

        if (dataCacheBeanMap == null || dataCacheBeanMap.size() == 0) {
            log.info("未发现需要注入的数据缓存对象");
            return;
        }

        for (Map.Entry<String, TenantDataCacheFactory> entry : dataCacheBeanMap.entrySet()) {
            TenantDataCacheFactory factory = entry.getValue();
            TenantDataCache dataCache = new TenantDataCache(factory, redisTemplate);
            Class dataClass = ReflectUtil.getSuperClassArgument(factory.getClass(), TenantDataCacheFactory.class, 0);
            register(dataCache, dataClass, factory.getCacheId());
        }

        log.info("自动注入数据缓存对象{}个", dataCacheBeanMap.size());
    }

}
