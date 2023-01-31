package com.luna.tenant.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.luna.tenant.service.dto.TenantUpdateDTO;
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
        Tenant tenant = SimpleBeanCopyUtil.simpleCopy(tenantDTO, new Tenant());
        tenant.setIsLocked(false);
        checkTenant(tenant);
        save(tenant);
    }

    /**
     * 租户更新
     *
     * @param tenantDTO
     */
    public void updateTenant(TenantUpdateDTO tenantDTO) {
        Tenant tenant = getWhole(tenantDTO.getId());
        SimpleBeanCopyUtil.simpleCopy(tenantDTO, tenant);
        checkTenant(tenant);
        updateWhole(tenant);
    }

    /**
     * 上锁或解锁租户
     *
     * @param id
     * @param isLock
     */
    public void lockTenant(long id, boolean isLock) {
        if (isLock) {
            getSqlMapper().update(null,
                    new LambdaUpdateWrapper<Tenant>()
                            .eq(Tenant::getId, id)
                            .set(Tenant::getIsLocked, true)
                            .set(Tenant::getIsEnabled, false)
            );
        } else {
            Tenant tenant = getWhole(id);
            tenant.setIsLocked(false);
            checkTenant(tenant);
            updateWhole(tenant);
        }
    }

    /**
     * 检查租户状态
     *
     * @param tenant
     */
    private void checkTenant(Tenant tenant) {
        Date expireDate = TimeUtil.toDate(tenant.getExpireDate());
        tenant.setExpireDate(expireDate);

        boolean isExpired = expireDate.getTime() < System.currentTimeMillis();
        boolean isLocked = tenant.getIsLocked();
        boolean isEnabled = !isExpired && !isLocked;
        tenant.setIsEnabled(isEnabled);

        if (isExpired) {
            tenant.setState(Tenant.STATE_EXPIRED);
        } else if (isLocked) {
            tenant.setState(Tenant.STATE_LOCKED);
        } else {
            tenant.setState(Tenant.STATE_ENABLED);
        }
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
        String server = serverService.getTenantServer(tenantId);
        dynamicHisServlet.postJsonRequest(server, InternalRequestPath.TENANT_MANAGER_INIT, createParam, String.class);
    }

}
