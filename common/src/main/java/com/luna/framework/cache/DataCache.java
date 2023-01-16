package com.luna.framework.cache;

/**
 * 数据缓存基本模型
 * <p>
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
public interface DataCache<T> {

    /**
     * 数据缓存唯一标识ID，默认实现类名
     */
    default String getId() {
        return this.getClass().getName();
    }

    /**
     * 当数据变化时应当触发该方法
     */
    default void dataChanged() {

    }

    /**
     * 加载数据
     */
    T loadData();

    /**
     * 获取缓存的数据
     */
    T getData();

}
