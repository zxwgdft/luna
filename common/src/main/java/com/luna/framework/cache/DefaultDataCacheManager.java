package com.luna.framework.cache;

import com.luna.framework.api.SystemException;
import com.luna.framework.utils.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 适合系统内部常用的数据缓存，例如常量、字典、标准等数据。
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
@Slf4j
public class DefaultDataCacheManager implements DataCacheManager {

    private Map<String, DataCache> id2CacheMap = new ConcurrentHashMap<>();
    private Map<String, DataCache> dataClass2CacheMap = new ConcurrentHashMap<>();


    @Override
    public <T> void register(DataCache<T> dataCache) {
        Class dataClass = ReflectUtil.getSuperClassArgument(dataCache.getClass(), DataCache.class, 0);
        register(dataCache, dataClass, dataCache.getId());
    }

    @Override
    public <T> void register(DataCache<T> dataCache, Class<T> dataClass, String cacheId) {
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

    @Override
    public <T> T getData(Class<T> dataClass) {
        String dataClassName = dataClass.getName();
        DataCache dataCache = dataClass2CacheMap.get(dataClassName);
        if (dataCache != null) {
            return (T) dataCache.getData();
        } else {
            log.error("找不到[class={}]对应的DataCache", dataClassName);
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
    }

    @Override
    public Object getData(String cacheId) {
        DataCache dataCache = id2CacheMap.get(cacheId);
        if (dataCache != null) {
            return dataCache.getData();
        } else {
            log.error("找不到[ID={}]对应的DataCache", cacheId);
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
    }

    @Override
    public <T> DataCache<T> getDataCache(Class<T> dataClazz) {
        return dataClass2CacheMap.get(dataClazz.getName());
    }

    @Override
    public DataCache getDataCache(String cacheId) {
        return id2CacheMap.get(cacheId);
    }

}
