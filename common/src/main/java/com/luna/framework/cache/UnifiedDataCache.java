package com.luna.framework.cache;

/**
 * 统一的缓存实现接口，实现该接口的缓存将按照统一的封装包装，
 * 省去了缓存更新同步的实现
 * @author Administrator
 */
public interface UnifiedDataCache<T> extends DataCache<T> {

    @Override
    default void dataChanged() {
        // 交由统一封装类负责
    }

    @Override
    default T getData() {
        // 交由统一封装类负责
        return null;
    }
}
