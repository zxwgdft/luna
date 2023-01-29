package com.luna.his.core.service;

import com.luna.constant.GlobalConstants;
import com.luna.framework.api.SystemException;
import com.luna.framework.utils.others.PinyinUtil;
import com.luna.his.api.HospitalCreateParam;
import com.luna.his.api.ManagerCreateParam;
import com.luna.his.core.EnvironmentUtil;
import com.luna.his.core.constant.EmployeeJob;
import com.luna.his.core.util.MultiIdUtil;
import com.luna.his.org.mapper.EmployeeMapper;
import com.luna.his.org.mapper.HospitalMapper;
import com.luna.his.org.model.Employee;
import com.luna.his.org.model.Hospital;
import com.luna.his.org.service.OrgRoleService;
import com.luna.tenant.api.AccountCreate;
import com.luna.tenant.client.AccountClient;
import com.luna.tenant.client.TenantClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService {

    private final HospitalMapper hospitalMapper;
    private final EmployeeMapper employeeMapper;
    private final OrgRoleService roleService;
    private final AccountClient accountClient;
    private final TenantClient tenantClient;

    /**
     * 创建诊所
     *
     * @param createParam
     */
    public void createHospital(HospitalCreateParam createParam) {
        long hospitalId = createParam.getHospitalId();
        try {
            Hospital hospital = hospitalMapper.selectById(hospitalId);
            if (hospital == null) {
                hospital = new Hospital();
                hospital.setId(createParam.getHospitalId());
                hospital.setName(createParam.getHospitalName());
                hospital.setOpenTime(640);
                hospital.setCloseTime(1080);
                hospital.setRestWeekdays("1");
                hospital.setTenantId(createParam.getTenantId());
                hospitalMapper.insert(hospital);
            }

            // 告诉租户模块
            tenantClient.createHospitalSuccessHandler(hospitalId);
        } catch (Exception e) {
            log.error("初始化诊所[id={}]失败", hospitalId, e);
            tenantClient.createHospitalFailHandler(hospitalId);
        }
    }

    /**
     * 创建诊所管理员
     *
     * @param createParam
     * @return
     */
    @Transactional
    public Employee createManager(ManagerCreateParam createParam) {
        Long hospitalId = createParam.getHospitalId();

        // 初始化角色
        // TODO 初始化所有系统角色，暂时只初始化管理员角色
        Long adminRoleId = roleService.initTenantRole(createParam.getTenantId());
        if (adminRoleId == null) {
            throw new SystemException(SystemException.CODE_ERROR_CODE, "管理员角色不存在");
        }

        Employee employee = new Employee();
        employee.setName(createParam.getName());
        employee.setNamePy(PinyinUtil.toPinyinInitial(employee.getName(), false));
        employee.setHospitalId(hospitalId);
        employee.setWorkHospitalIds(null); // 管理员工作诊所为所有
        employee.setJob(EmployeeJob.MANAGER.getCode());
        employee.setAccount(createParam.getAccount());
        employee.setCellphone(createParam.getCellphone());
        employee.setTenantId(createParam.getTenantId());
        employee.setRoleIds(MultiIdUtil.joinId2Str(new long[]{adminRoleId}));
        employee.setWorkScope(GlobalConstants.WORK_SCOPE_TENANT);

        employeeMapper.insert(employee);

        // 创建账号
        AccountCreate accountCreate = new AccountCreate();
        accountCreate.setAccount(createParam.getAccount());
        accountCreate.setPassword(createParam.getPassword());
        accountCreate.setCellphone(createParam.getCellphone());
        accountCreate.setUserId(employee.getId());
        accountCreate.setType(GlobalConstants.USER_TYPE_EMPLOYEE);
        accountCreate.setTenantId(employee.getTenantId());
        accountCreate.setServer(EnvironmentUtil.getAppServerName());
        accountClient.createAccount(accountCreate);

        return employee;
    }


}
