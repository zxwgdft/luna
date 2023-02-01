package com.luna.tenant.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.utils.TimeUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.api.InternalRequestPath;
import com.luna.his.api.ManagerCreateParam;
import com.luna.his.api.TenantManager;
import com.luna.his.api.TenantManagers;
import com.luna.tenant.mapper.TenantMapper;
import com.luna.tenant.model.Hospital;
import com.luna.tenant.model.Tenant;
import com.luna.tenant.service.dto.HospitalDTO;
import com.luna.tenant.service.dto.TenantDTO;
import com.luna.tenant.service.dto.TenantManagerDTO;
import com.luna.tenant.service.dto.TenantUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService extends ServiceSupport<Tenant, TenantMapper> {

    private final DynamicHisServlet dynamicHisServlet;
    private final ServerService serverService;
    private final HospitalService hospitalService;

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
     * 创建租户总部诊所
     *
     * @param hospitalDTO
     */
    public void createHeadquarter(HospitalDTO hospitalDTO) {
        long tenantId = hospitalDTO.getTenantId();
        Tenant tenant = get(tenantId);
        if (tenant.getHeadquarterId() != null) {
            throw new BusinessException("租户总部已经存在，不能重复创建！");
        }
        Hospital hospital = hospitalService.createHospital(hospitalDTO, true);
        getSqlMapper().update(null,
                new LambdaUpdateWrapper<Tenant>()
                        .eq(Tenant::getId, tenantId)
                        .isNull(Tenant::getHeadquarterId)
                        .set(Tenant::getHeadquarterId, hospital.getId())
        );
    }

    /**
     * 初始化租户
     *
     * @param param
     */
    public void createManager(TenantManagerDTO param) {
        ManagerCreateParam createParam = SimpleBeanCopyUtil.simpleCopy(param, new ManagerCreateParam());
        long tenantId = param.getTenantId();
        Tenant tenant = get(tenantId);
        Long headquarterId = tenant.getHeadquarterId();
        if (headquarterId == null) {
            throw new BusinessException("租户总部未创建成功，请确认创建成功后再创建管理员");
        }
        Hospital hospital = hospitalService.get(headquarterId);
        if (hospital.getState() != Hospital.STATE_CREATE_SUCCESS) {
            throw new BusinessException("租户总部未创建成功，请确认创建成功后再创建管理员");
        }
        createParam.setHospitalId(headquarterId);
        String server = serverService.getTenantServer(tenantId);
        dynamicHisServlet.postJsonRequest(server, InternalRequestPath.TENANT_MANAGER_INIT, createParam, String.class);
    }

    /**
     * 获取租户的管理员列表
     *
     * @param tenantId
     * @return
     */
    public List<TenantManager> getTenantManagers(long tenantId) {
        String server = serverService.getTenantServer(tenantId);
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("tenantId", tenantId);
        TenantManagers managers = dynamicHisServlet.getRequest(server, InternalRequestPath.GET_TENANT_MANAGERS,
                uriVariables, TenantManagers.class);
        return managers.getData();
    }

}
