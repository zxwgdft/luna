package com.luna.framework.spring;

import com.luna.framework.cache.DataCache;
import com.luna.framework.cache.DataCacheManager;
import com.luna.framework.cache.DefaultDataCacheWrapper;
import com.luna.framework.utils.reflect.ReflectUtil;
import com.luna.framework.cache.UnifiedDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Map;

@Slf4j
public class DataCacheFactory implements ApplicationRunner {

    @Autowired
    private DataCacheManager dataCacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("扫描数据缓存对象...");

        Map<String, DataCache> dataCacheBeanMap = SpringBeanHelper.getBeansByType(DataCache.class);

        if (dataCacheBeanMap == null || dataCacheBeanMap.size() == 0) {
            log.info("未发现需要注入的数据缓存对象");
            return;
        }

        for (Map.Entry<String, DataCache> entry : dataCacheBeanMap.entrySet()) {
            DataCache dataCache = entry.getValue();
            if (dataCache instanceof UnifiedDataCache) {
                DataCache dataCacheWrapper = wrapDataCache(dataCache);
                Class dataClass = ReflectUtil.getSuperClassArgument(dataCache.getClass(), UnifiedDataCache.class, 0);
                dataCacheManager.register(dataCacheWrapper, dataClass, dataCache.getId());
            } else {
                dataCacheManager.register(dataCache);
            }
        }

        log.info("自动注入数据缓存对象{}个", dataCacheBeanMap.size());
    }

    public DataCache wrapDataCache(DataCache dataCache) {
        return new DefaultDataCacheWrapper(dataCache);
    }
}
