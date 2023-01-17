package com.luna.his.org.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.constant.GlobalConstants;
import com.luna.framework.api.PageResult;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.service.QueryWrapperHelper;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.framework.utils.others.PinyinUtil;
import com.luna.his.core.EnvironmentUtil;
import com.luna.his.core.framework.HisServiceSupport;
import com.luna.his.core.log.*;
import com.luna.his.core.util.MultiIdUtil;
import com.luna.his.org.mapper.EmployeeMapper;
import com.luna.his.org.model.Employee;
import com.luna.his.org.service.dto.EmployeeAccountDTO;
import com.luna.his.org.service.dto.EmployeeAccountLO;
import com.luna.his.org.service.dto.EmployeeDTO;
import com.luna.his.org.service.dto.EmployeeQuery;
import com.luna.tenant.api.AccountCreate;
import com.luna.tenant.api.AccountUpdate;
import com.luna.tenant.client.AccountClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工相关服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService extends HisServiceSupport<Employee, EmployeeMapper> {

    private final AccountClient accountClient;

    /**
     * 获取员工信息
     *
     * @param id 员工ID
     * @return
     */
    public Employee getEmployee(Long id) {
        return findOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getId, id)
        );
    }

    /**
     * 查询员工列表
     *
     * @param employeeQuery 员工查询条件
     * @return 员工列表（简单信息）
     */
    public List<Employee> findSimpleEmployeeList(EmployeeQuery employeeQuery) {
        return findSimpleList(convertQueryWrapper(employeeQuery));
    }

    /**
     * 查询员工信息
     *
     * @param employeeQuery
     * @return
     */
    public List<Employee> findEmployeeList(EmployeeQuery employeeQuery) {
        return findList(convertQueryWrapper(employeeQuery));
    }

    /**
     * 查询员工分页列表
     *
     * @param employeeQuery 员工查询条件
     * @return 员工分页列表
     */
    public PageResult<Employee> findEmployeePage(EmployeeQuery employeeQuery) {
        return findPage(employeeQuery, convertQueryWrapper(employeeQuery));
    }

    /**
     * 转换参数为查询条件
     *
     * @param employeeQuery 查询参数
     * @return 查询条件
     */
    private Wrapper<Employee> convertQueryWrapper(EmployeeQuery employeeQuery) {
        LambdaQueryWrapper<Employee> queryWrapper = QueryWrapperHelper.buildQuery(employeeQuery).lambda();

        // 姓名查询需要关联起拼音
        String name = employeeQuery.getName();
        if (StringUtil.isNotEmpty(name)) {
            if (StringUtil.isContainsChinese(name)) {
                queryWrapper.like(Employee::getName, name);
            } else {
                queryWrapper.and(
                        wrapper -> wrapper
                                .like(Employee::getName, name)
                                .or()
                                .likeLeft(Employee::getNamePy, name)
                );
            }
        }

        // 角色字段为逗号拼接字符串，这里使用like查询
        Long roleId = employeeQuery.getRoleId();
        if (roleId != null) {
            queryWrapper.like(Employee::getRoleIds, MultiIdUtil.getLikeSql(roleId));
        }

        return queryWrapper;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息
     */
    @Transactional
    @ActionLog(module = Module.EMPLOYEE_MANAGE, actionType = ActionType.ADD, action = "新增员工", hasContent = true)
    public void addEmployee(EmployeeDTO employeeDTO) {
        checkHospital(employeeDTO);

        Employee employee = SimpleBeanCopyUtil.simpleCopy(employeeDTO, new Employee());
        employee.setNamePy(PinyinUtil.toPinyinInitial(employee.getName(), false));
        employee.setWorkHospitalIds(MultiIdUtil.joinId2Str(employeeDTO.getWorkHospitalIds()));
        employee.setWorkScope(GlobalConstants.WORK_SCOPE_HOSPITAL);

        save(employee);

        // 设置添加内容到操作日志
        OperateLogHelper.setDataUpdate(new DataUpdate(null, employee, null));
    }

    /**
     * 修改员工
     *
     * @param employeeDTO 员工信息
     */
    @Transactional
    @ActionLog(module = Module.EMPLOYEE_MANAGE, actionType = ActionType.UPDATE, action = "修改员工", hasContent = true)
    public void updateEmployee(EmployeeDTO employeeDTO) {
        checkHospital(employeeDTO);

        Long id = employeeDTO.getId();
        Employee origin = getWhole(id);
        Employee current = SimpleBeanCopyUtil.simpleCopy(origin, new Employee());
        SimpleBeanCopyUtil.simpleCopy(employeeDTO, current);
        current.setNamePy(PinyinUtil.toPinyinInitial(current.getName(), false));
        current.setWorkHospitalIds(MultiIdUtil.joinId2Str(employeeDTO.getWorkHospitalIds()));
        updateWhole(current);

        // 设置更新内容到操作日志
        OperateLogHelper.setDataUpdate(new DataUpdate(origin, current, null));
    }

    /**
     * 校验工作诊所
     *
     * @param employeeDTO
     */
    private void checkHospital(EmployeeDTO employeeDTO) {
        long hospitalId = employeeDTO.getHospitalId();
        for (Long id : employeeDTO.getWorkHospitalIds()) {
            if (hospitalId == id.longValue()) {
                return;
            }
        }
        throw new BusinessException("员工的工作诊所必须包含所属诊所");
    }

    /**
     * 创建或更新员工用户账号
     *
     * @param accountDTO
     */
    @Transactional
    @ActionLog(module = Module.EMPLOYEE_MANAGE, actionType = ActionType.UPDATE, action = "修改员工账号", hasContent = true)
    public void createOrUpdateEmployeeAccount(EmployeeAccountDTO accountDTO) {
        Long employeeId = accountDTO.getId();
        String account = accountDTO.getAccount();
        String cellphone = accountDTO.getCellphone();
        String roleIds = MultiIdUtil.joinId2Str(accountDTO.getRoleIds());

        Employee employee = getWhole(employeeId);
        String originAccount = employee.getAccount();

        if (StringUtil.isEmpty(cellphone)) {
            throw new BusinessException("用户手机不能为空");
        }

        // 用于操作日志
        EmployeeAccountLO originAccountLO = new EmployeeAccountLO();
        originAccountLO.setAccount(originAccount);
        originAccountLO.setCellphone(employee.getCellphone());
        originAccountLO.setRoleIds(employee.getRoleIds());

        EmployeeAccountLO currentAccountLO = new EmployeeAccountLO();
        currentAccountLO.setAccount(account);
        currentAccountLO.setCellphone(cellphone);
        currentAccountLO.setRoleIds(roleIds);


        // TODO 校验手机是否重复
        // TODO 发送账号开通短信

        if (StringUtil.isEmpty(originAccount)) {
            String password = accountDTO.getPassword();
            if (StringUtil.isEmpty(password)) {
                throw new BusinessException("账号密码不能为空");
            }

            // 原账号为空则创建用户账号
            employee.setAccount(account);
            employee.setCellphone(cellphone);

            // 创建账号
            AccountCreate accountCreate = new AccountCreate();
            accountCreate.setAccount(account);
            accountCreate.setPassword(accountDTO.getPassword());
            accountCreate.setCellphone(cellphone);
            accountCreate.setUserId(employeeId);
            accountCreate.setType(GlobalConstants.USER_TYPE_EMPLOYEE);
            accountCreate.setTenantId(employee.getTenantId());
            accountCreate.setServer(EnvironmentUtil.getAppServerName());
            accountClient.createAccount(accountCreate);

            // 更新员工账号和手机信息
            getSqlMapper().update(null,
                    new LambdaUpdateWrapper<Employee>()
                            .eq(Employee::getId, employeeId)
                            .set(Employee::getAccount, account)
                            .set(Employee::getCellphone, cellphone)
                            .set(Employee::getRoleIds, roleIds)
            );

        } else {
            // 更新员工账号和手机信息
            getSqlMapper().update(null,
                    new LambdaUpdateWrapper<Employee>()
                            .eq(Employee::getId, employeeId)
                            .set(Employee::getAccount, account)
                            .set(Employee::getCellphone, cellphone)
                            .set(Employee::getRoleIds, roleIds)
            );

            // 更新账号信息
            AccountUpdate accountUpdate = new AccountUpdate();
            accountUpdate.setAccount(account);
            accountUpdate.setCellphone(cellphone);
            accountUpdate.setUserId(employeeId);
            accountUpdate.setType(GlobalConstants.USER_TYPE_EMPLOYEE);
            accountClient.updateAccount(accountUpdate);
        }

        OperateLogHelper.setDataUpdate(new DataUpdate(originAccountLO, currentAccountLO, null));
    }


}