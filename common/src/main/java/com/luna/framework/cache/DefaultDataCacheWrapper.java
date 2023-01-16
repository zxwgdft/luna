package com.luna.framework.cache;

/**
 * 基于内存的缓存包装类
 */
public class DefaultDataCacheWrapper<T> implements DataCache<T> {

    private DataCache<T> source;
    private T data;
    private volatile boolean loaded = false;

    public DefaultDataCacheWrapper(DataCache<T> dataCache) {
        source = dataCache;
    }

    @Override
    public String getId() {
        return source.getId();
    }

    @Override
    public void dataChanged() {
        loaded = false;
    }

    @Override
    public T loadData() {
        return source.loadData();
    }

    @Override
    public T getData() {
        if (!loaded) {
            data = source.loadData();
            loaded = true;
        }
        return data;
    }


}
