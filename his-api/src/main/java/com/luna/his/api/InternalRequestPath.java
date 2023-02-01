package com.luna.his.api;

/**
 * @author TontoZhou
 */
public interface InternalRequestPath {


    /**
     * 租户诊所初始化地址
     */
    String TENANT_HOSPITAL_INIT = "/internal/tenant/hospital/init";

    /**
     * 租户管理员初始化地址
     */
    String TENANT_MANAGER_INIT = "/internal/tenant/manager/init";

    /**
     * 获取用户token
     */
    String GET_USER_TOKEN = "/internal/get/token";

    /**
     * 获取租户管理员列表
     */
    String GET_TENANT_MANAGERS = "/internal/get/managers/{tenantId}";
}
