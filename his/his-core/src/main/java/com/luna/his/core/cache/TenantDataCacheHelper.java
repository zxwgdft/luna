package com.luna.his.core.cache;

import com.luna.framework.api.SystemException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.his.core.HisUserSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * 提供静态方法访问租户的缓存数据
 *
 * @author TontoZhou
 * @since 2021/6/4
 */
public class TenantDataCacheHelper {

    @Autowired
    private TenantDataCacheManager cacheManager;

    private static TenantDataCacheManager instance;

    @PostConstruct
    public void init() {
        instance = cacheManager;
    }

    /**
     * @param dataClazz 缓存数据类型
     * @param <T>       缓存数据类型
     * @return 缓存数据
     */
    public static <T> T getData(Class<T> dataClazz) {
        return instance.getDataCache(dataClazz).getData(((HisUserSession) WebSecurityManager.getCurrentUserSession()).getTenantId());
    }

    /**
     * @param dataClazz 缓存数据类型
     * @param tenantId  租户ID
     * @param <T>       缓存数据类型
     * @return 缓存数据
     */
    public static <T> T getData(Class<T> dataClazz, long tenantId) {
        return instance.getDataCache(dataClazz).getData(tenantId);
    }

    /**
     * @param dataClazz 缓存数据类型
     */
    public static void dataChanged(Class<?> dataClazz) {
        instance.getDataCache(dataClazz).dataChanged(((HisUserSession) WebSecurityManager.getCurrentUserSession()).getTenantId());
    }

    /**
     * @param dataClazz 缓存数据类型
     * @param tenantId  租户ID
     */
    public static void dataChanged(Class<?> dataClazz, long tenantId) {
        instance.getDataCache(dataClazz).dataChanged(tenantId);
    }

    /**
     * 获取请求级别缓存数据
     * <p>
     * 忽略请求间数据变更的变更，在同一请求下只尝试获取一次缓存数据
     * <p>
     * 注意：如果请求未经过WebSecurityManager则不应该调用该方法
     *
     * @param dataClass 缓存数据类型
     * @param <T>       缓存数据类型
     * @return 缓存数据
     */
    public static <T> T getRequestCacheData(Class<T> dataClass) {
        HttpServletRequest request = WebSecurityManager.getCurrentRequest();
        if (request != null) {
            String field = dataClass.getName();
            T data = (T) request.getAttribute(field);
            if (data == null) {
                long tenantId = ((HisUserSession) WebSecurityManager.getCurrentUserSession()).getTenantId();
                data = instance.getDataCache(dataClass).getData(tenantId);
                request.setAttribute(field, data);
            }
            return data;
        } else {
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
    }

}
