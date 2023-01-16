package com.luna.tenant.service;

import com.luna.framework.exception.BusinessException;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.utils.TimeUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.api.InternalRequestPath;
import com.luna.his.api.ManagerCreateParam;
import com.luna.tenant.mapper.TenantMapper;
import com.luna.tenant.model.Tenant;
import com.luna.tenant.model.TenantHospital;
import com.luna.tenant.service.dto.TenantDTO;
import com.luna.tenant.service.dto.TenantManagerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService extends ServiceSupport<Tenant, TenantMapper> {

    private final DynamicHisServlet dynamicHisServlet;
    private final ServerService serverService;
    private final TenantHospitalService tenantHospitalService;

    /**
     * 新增租户
     *
     * @param tenantDTO
     */
    public void addTenant(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        tenant.setName(tenantDTO.getName());
        tenant.setServerId(tenantDTO.getServerId());

        int years = tenantDTO.getExpireYears();
        Date expireDate = TimeUtil.getDateBeforeYear(new Date(), -years);

        tenant.setExpireDate(expireDate);
        tenant.setState(Tenant.STATE_NONE);

        save(tenant);
    }

    /**
     * 初始化租户
     *
     * @param param
     */
    public void createManager(TenantManagerDTO param) {
        ManagerCreateParam createParam = SimpleBeanCopyUtil.simpleCopy(param, new ManagerCreateParam());
        long tenantId = param.getTenantId();
        TenantHospital hospital = tenantHospitalService.getHeadquarter(tenantId);
        if (hospital == null) {
            throw new BusinessException("必须先创建租户的总部诊所后才能创建管理员");
        }
        if (hospital.getState() != TenantHospital.STATE_CREATE_SUCCESS) {
            throw new BusinessException("租户总部未创建成功，请确认创建成功后再创建管理员");
        }
        createParam.setHospitalId(hospital.getId());
        String appName = serverService.getTenantAppName(tenantId);
        dynamicHisServlet.postJsonRequest(appName, InternalRequestPath.TENANT_MANAGER_INIT, createParam, String.class);
    }

}
