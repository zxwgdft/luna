package com.luna.framework.cache;

/**
 * 数据缓存管理器
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
public interface DataCacheManager {

    /**
     * 注册数据缓存对象
     */
    <T> void register(DataCache<T> dataCache);

    /**
     * 注册数据缓存对象
     */
    <T> void register(DataCache<T> dataCache, Class<T> dataClass, String cacheId);


    /**
     * 通过ID获取数据缓存对象
     */
    <T> DataCache<T> getDataCache(String id);

    /**
     * 通过数据类型获取数据缓存对象
     */
    <T> DataCache<T> getDataCache(Class<T> dataClazz);

    /**
     * 通过ID获取缓存数据
     */
    <T> T getData(String id);

    /**
     * 通过数据类型获取缓存数据
     */
    <T> T getData(Class<T> dataClazz);

}
