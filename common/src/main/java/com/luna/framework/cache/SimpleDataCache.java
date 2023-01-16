package com.luna.framework.cache;

/**
 * 简单数据缓存基本模型
 * <p>
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
public abstract class SimpleDataCache<T> implements DataCache<T> {

    // TODO 提供对简单缓存的更新策略

    private volatile T data;

    @Override
    public void dataChanged() {
        data = loadData();
    }

    @Override
    public T getData() {
        if (data == null) {
            data = loadData();
        }
        return data;
    }

}
