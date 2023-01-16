package com.luna.tenant.service;

import com.luna.framework.service.ServiceSupport;
import com.luna.tenant.mapper.ServerMapper;
import com.luna.tenant.model.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerService extends ServiceSupport<Server, ServerMapper> {


    /**
     * 获取租户对应的应用服务器名称
     *
     * @param tenantId
     * @return
     */
    public String getTenantAppName(long tenantId) {
        return getSqlMapper().getServerAppNameByTenant(tenantId);
    }


}
