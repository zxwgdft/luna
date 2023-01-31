package com.luna.tenant.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.exception.IllegalRequestException;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.api.HospitalCreateParam;
import com.luna.his.api.InternalRequestPath;
import com.luna.tenant.mapper.HospitalMapper;
import com.luna.tenant.model.Hospital;
import com.luna.tenant.service.dto.HospitalDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalService extends ServiceSupport<Hospital, HospitalMapper> {

    private final DynamicHisServlet dynamicHisServlet;
    private final ServerService serverService;

    /**
     * 创建诊所
     *
     * @param hospitalDTO
     */
    @Transactional
    public Hospital createHospital(HospitalDTO hospitalDTO, boolean isHeadquarter) {
        String restWeekdays = convertRestWeekdays(hospitalDTO.getRestWeekdays());
        Hospital hospital = SimpleBeanCopyUtil.simpleCopy(hospitalDTO, new Hospital());
        hospital.setState(Hospital.STATE_INITIALIZING);
        hospital.setIsHeadquarter(isHeadquarter);
        hospital.setRestWeekdays(restWeekdays);
        save(hospital);
        _initHospital(hospital);
        return hospital;
    }

    /**
     * 转换休息日为拼接字符串（用于存储）
     *
     * @param restDays
     * @return
     */
    private static String convertRestWeekdays(List<Integer> restDays) {
        String restWeekdays = null;
        if (restDays != null && restDays.size() > 0) {
            Set<Integer> days = new HashSet<>();
            for (Integer day : restDays) {
                if (day < 1 || day > 7) {
                    throw new IllegalRequestException();
                }
                days.add(day);
            }
            if (days.size() > 7) {
                throw new IllegalRequestException();
            }
            restWeekdays = StringUtil.join(days);
        }
        return restWeekdays;
    }

    /**
     * 初始化诊所
     * <p>
     * 该方法应该在某些意外情况下诊所未开始初始化时被调用
     *
     * @param hospitalId
     */
    public void initHospital(int hospitalId) {
        Hospital hospital = get(hospitalId);
        int state = hospital.getState();
        if (state == Hospital.STATE_CREATE_FAIL) {
            _initHospital(hospital);
        } else if (state == Hospital.STATE_INITIALIZING) {
            throw new BusinessException("诊所正在初始化中，请不要重复操作");
        } else if (state == Hospital.STATE_CREATE_SUCCESS) {
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
    private void _initHospital(Hospital hospital) {
        // 找到对应服务进行初始化任务，这里可以改为mq形式
        HospitalCreateParam createParam = new HospitalCreateParam();
        createParam.setHospitalId(hospital.getId());
        createParam.setHospitalName(hospital.getName());
        createParam.setTenantId(hospital.getTenantId());

        String server = serverService.getTenantServer(hospital.getTenantId());
        dynamicHisServlet.postJsonRequest(server, InternalRequestPath.TENANT_HOSPITAL_INIT, createParam, String.class);

        // 更新状态为初始化中
        getSqlMapper().update(null,
                new LambdaUpdateWrapper<Hospital>()
                        .eq(Hospital::getId, hospital.getId())
                        .set(Hospital::getState, Hospital.STATE_INITIALIZING)
        );
    }

    /**
     * 处理诊所初始化完毕方法
     *
     * @param hospitalId
     * @param success    是否成功
     */
    public void handleHospitalInitialized(long hospitalId, boolean success) {
        int state = success ? Hospital.STATE_CREATE_SUCCESS : Hospital.STATE_CREATE_FAIL;
        // 更新状态为初始化中
        getSqlMapper().update(null,
                new LambdaUpdateWrapper<Hospital>()
                        .eq(Hospital::getId, hospitalId)
                        .set(Hospital::getState, state)
        );
    }

}
