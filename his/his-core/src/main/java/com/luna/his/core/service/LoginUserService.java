package com.luna.his.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luna.constant.GlobalConstants;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.utils.StringUtil;
import com.luna.his.api.AuthenticatedUser;
import com.luna.his.core.HisUserClaims;
import com.luna.his.core.HisUserSession;
import com.luna.his.core.cache.TenantDataCacheHelper;
import com.luna.his.core.log.LogConstants;
import com.luna.his.core.permission.Role;
import com.luna.his.core.permission.RoleContainer;
import com.luna.his.core.service.dto.LoginUser;
import com.luna.his.core.util.MultiIdUtil;
import com.luna.his.org.model.Employee;
import com.luna.his.org.model.Hospital;
import com.luna.his.org.service.EmployeeService;
import com.luna.his.org.service.HospitalService;
import com.luna.his.search.entity.LogLogin;
import com.luna.his.search.service.LogSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserService {

    @Value("${his.auth.token-expire-milliseconds:3600000}")
    private int tokenExpireMilliseconds;

    @Value("${his.auth.token-long-expire-milliseconds:3600000}")
    private int tokenLongExpireMilliseconds;

    private final EmployeeService employeeService;
    private final HospitalService hospitalService;
    private final LogSearchService logSearchService;
    @Autowired(required = false)
    private WebSecurityManager securityManager;

    /**
     * 获取登录用户信息
     */
    public LoginUser getLoginUser() {
        // TODO 每次页面刷新都会调用该方法，并且该方法不经常变更，这里需要做数据缓存
        // 该信息用于员工登录的系统，这里不处理其他类型用户
        HisUserSession userSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();

        Employee employee = employeeService.get(userSession.getUserId());
        // TODO 判断是否员工账号还可用
        List<Hospital> workHospitals = null;

        int workScope = employee.getWorkScope();
        if (workScope == GlobalConstants.WORK_SCOPE_TENANT) {
            workHospitals = hospitalService.findSimpleList(
                    new LambdaQueryWrapper<Hospital>()
                            .eq(Hospital::getIsEnabled, true)
            );
        } else {
            String workHospitalIds = employee.getWorkHospitalIds();
            workHospitals = hospitalService.findSimpleList(
                    new LambdaQueryWrapper<Hospital>()
                            .eq(Hospital::getIsEnabled, true)
                            .in(Hospital::getId, MultiIdUtil.split2IntList(workHospitalIds))
            );
        }

        if (workHospitals == null || workHospitals.size() == 0) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "用户账号异常，没有可用工作诊所");
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setWorkHospitals(workHospitals);
        loginUser.setCurHospitalId(userSession.getHospitalId());
        loginUser.setUsername(userSession.getUserName());
        loginUser.setIsAdmin(false);

        // 获取权限
        RoleContainer roleContainer = TenantDataCacheHelper.getRequestCacheData(RoleContainer.class);
        if (userSession.getRoleIds().size() == 1) {
            for (Long roleId : userSession.getRoleIds()) {
                Role role = roleContainer.getRole(roleId);
                if (role != null) {
                    if (role.getIsAdmin()) {
                        loginUser.setIsAdmin(true);
                        // 管理员默认拥有所有权限，这里不需要设置权限编码
                    } else {
                        loginUser.setPermissionCodes(role.getCodes());
                    }
                }
            }
        } else {
            Set<String> codes = new HashSet<>();
            boolean isAdmin = false;
            for (Long roleId : userSession.getRoleIds()) {
                Role role = roleContainer.getRole(roleId);
                if (role != null) {
                    if (role.getIsAdmin()) {
                        isAdmin = true;
                        break;
                    }
                    codes.addAll(role.getCodes());
                }
            }

            if (isAdmin) {
                loginUser.setIsAdmin(true);
                // 管理员默认拥有所有权限，这里不需要设置权限编码
            } else {
                loginUser.setPermissionCodes(codes);
            }
        }

        return loginUser;
    }

    /**
     * 设置当前用户工作诊所
     *
     * @param hospitalId
     * @return 返回新的token
     */
    public String setCurHospital(long hospitalId) {
        // TODO 判断诊所权限，是否为自己的工作诊所
        HisUserSession userSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
        Hospital hospital = hospitalService.get(hospitalId);
        if (hospital == null || !hospital.getIsEnabled()) {
            throw new BusinessException("无法切换到选择诊所，诊所不可用");
        }

        HisUserClaims userClaims = new HisUserClaims(
                userSession.getUserId(),
                -1L,
                StringUtil.join(userSession.getRoleIds()),
                userSession.getUserName(),
                userSession.getUserNo(),
                userSession.getWorkScope(),
                hospitalId,
                hospital.getName(),
                userSession.getTenantId()
        );

        return securityManager.createToken(userClaims);
    }

    /**
     * 获取用户token
     *
     * @return 返回token
     */
    public String getUserToken(AuthenticatedUser user) {
        long employeeId = user.getEmployeeId();
        Employee employee = employeeService.getWhole(employeeId);
        long hospitalId = employee.getHospitalId();
        Hospital hospital = hospitalService.get(hospitalId);
        if (hospital == null || !hospital.getIsEnabled()) {
            throw new BusinessException("账号异常，所属诊所不可用");
        }

        // 记录日志
        saveLoginLog(employee, hospital, user);

        long expiresAt = user.isRememberMe() ?
                (System.currentTimeMillis() + tokenLongExpireMilliseconds) :
                (System.currentTimeMillis() + tokenExpireMilliseconds);

        HisUserClaims userClaims = new HisUserClaims(employeeId, expiresAt, employee.getRoleIds(), employee.getName(),
                employee.getEmployeeNo(), employee.getWorkScope(), hospitalId, hospital.getName(), employee.getTenantId());

        return securityManager.createToken(userClaims);
    }

    private void saveLoginLog(Employee employee, Hospital hospital, AuthenticatedUser user) {
        LogLogin logData = new LogLogin();

        logData.setUserId(employee.getId());
        logData.setUserName(employee.getName());
        logData.setUserNo(employee.getEmployeeNo());
        logData.setPlatform(LogConstants.PLATFORM_SAAS);
        logData.setHospitalId(hospital.getId());
        logData.setHospitalName(hospital.getName());
        logData.setDeviceId(user.getDeviceId());
        logData.setDeviceCode(user.getDeviceCode());
        logData.setIp(user.getIp());
        // TODO ip属地功能待做
        logData.setIpLocation("");
        logData.setOperateTime(new Date());
        logData.setAction(LogConstants.LOGIN_ACTION_LOGIN);
        logData.setResult(LogConstants.LOGIN_RESULT_SUCCESS);
        logData.setRemark("");
        logData.setTenantId(employee.getTenantId());

        logSearchService.saveLoginLog(logData);
    }

}
