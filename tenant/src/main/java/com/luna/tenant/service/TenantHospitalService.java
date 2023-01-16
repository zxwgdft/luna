package com.luna.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.api.HospitalCreateParam;
import com.luna.his.api.InternalRequestPath;
import com.luna.tenant.mapper.TenantHospitalMapper;
import com.luna.tenant.model.TenantHospital;
import com.luna.tenant.service.dto.TenantHospitalDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantHospitalService extends ServiceSupport<TenantHospital, TenantHospitalMapper> {

    private final DynamicHisServlet dynamicHisServlet;
    private final ServerService serverService;


    /**
     * 创建诊所
     *
     * @param tenantHospitalDTO
     */
    public void createHospital(TenantHospitalDTO tenantHospitalDTO) {
        if (tenantHospitalDTO.getIsHeadquarter()) {
            // 查询是否有其他总部存在
            if (getHeadquarterId(tenantHospitalDTO.getTenantId()) != null) {
                throw new BusinessException("该租户已经存在一个总部诊所或医院");
            }
        }

        TenantHospital hospital = SimpleBeanCopyUtil.simpleCopy(tenantHospitalDTO, new TenantHospital());
        hospital.setState(TenantHospital.STATE_NONE);
        save(hospital);

        _initHospital(hospital);
    }

    /**
     * 初始化诊所
     * <p>
     * 该方法应该在某些意外情况下诊所未开始初始化时被调用
     *
     * @param hospitalId
     */
    public void initHospital(int hospitalId) {
        TenantHospital hospital = get(hospitalId);
        int state = hospital.getState();
        if (state == TenantHospital.STATE_NONE || state == TenantHospital.STATE_CREATE_FAIL) {
            _initHospital(hospital);
        } else if (state == TenantHospital.STATE_INITIALIZING) {
            throw new BusinessException("诊所正在初始化中，请不要重复操作");
        } else if (state == TenantHospital.STATE_CREATE_SUCCESS) {
            throw new BusinessException("诊所已经初始化创建成功，请不要重复操作");
        } else {
            throw new BusinessException("诊所状态无法初始化");
        }
    }

    /**
     * 真正的初始化诊所功能
     *
     * @param hospital
     */
    private void _initHospital(TenantHospital hospital) {
        // 找到对应服务进行初始化任务，这里可以改为mq形式
        HospitalCreateParam createParam = new HospitalCreateParam();
        createParam.setHospitalId(hospital.getId());
        createParam.setHospitalName(hospital.getName());
        createParam.setTenantId(hospital.getTenantId());

        String appName = serverService.getTenantAppName(hospital.getTenantId());
        dynamicHisServlet.postJsonRequest(appName, InternalRequestPath.TENANT_HOSPITAL_INIT, createParam, String.class);

        // 更新状态为初始化中
        getSqlMapper().update(null,
                new LambdaUpdateWrapper<TenantHospital>()
                        .eq(TenantHospital::getId, hospital.getId())
                        .set(TenantHospital::getState, TenantHospital.STATE_INITIALIZING)
        );
    }

    /**
     * 处理诊所初始化完毕方法
     *
     * @param hospitalId
     * @param success    是否成功
     */
    public void handleHospitalInitialized(long hospitalId, boolean success) {
        int state = success ? TenantHospital.STATE_CREATE_SUCCESS : TenantHospital.STATE_CREATE_FAIL;
        // 更新状态为初始化中
        getSqlMapper().update(null,
                new LambdaUpdateWrapper<TenantHospital>()
                        .eq(TenantHospital::getId, hospitalId)
                        .set(TenantHospital::getState, state)
        );
    }

    /**
     * 获取总部诊所ID
     *
     * @param tenantId
     * @return
     */
    public Long getHeadquarterId(long tenantId) {
        return getSqlMapper().getHeadquarterId(tenantId);
    }

    /**
     * 获取总部医院数据
     *
     * @param tenantId
     * @return
     */
    public TenantHospital getHeadquarter(long tenantId) {
        return getSqlMapper().selectOne(
                new LambdaQueryWrapper<TenantHospital>()
                        .eq(TenantHospital::getTenantId, tenantId)
                        .eq(TenantHospital::getIsHeadquarter, true)
        );
    }
}
