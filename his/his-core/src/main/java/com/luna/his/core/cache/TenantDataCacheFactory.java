package com.luna.his.core.cache;

import com.luna.framework.cache.DataCache;

/**
 * @author TontoZhou
 */
public interface TenantDataCacheFactory<T> {

    /**
     * 缓存ID
     *
     * @return
     */
    String getCacheId();

    /**
     * 创建缓存
     *
     * @param tenantId
     * @return
     */
    DataCache<T> createDataCache(long tenantId);

}
