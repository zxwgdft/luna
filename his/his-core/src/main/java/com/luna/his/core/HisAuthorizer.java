package com.luna.his.core;

import com.luna.constant.GlobalConstants;
import com.luna.framework.api.AuthenticationToken;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.security.Authorizer;
import com.luna.framework.security.UserClaims;
import com.luna.his.org.model.Employee;
import com.luna.his.org.model.Hospital;
import com.luna.his.org.service.EmployeeService;
import com.luna.his.org.service.HospitalService;
import com.luna.tenant.api.AccountUser;
import com.luna.tenant.api.PasswordToken;
import com.luna.tenant.client.AccountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class HisAuthorizer implements Authorizer {

    @Value("${his.auth.token-expire-milliseconds:3600000}")
    private int tokenExpireMilliseconds;

    @Value("${his.auth.token-long-expire-milliseconds:3600000}")
    private int tokenLongExpireMilliseconds;

    @Autowired
    private AccountClient accountClient;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private EmployeeService employeeService;


    @Override
    public UserClaims authorize(AuthenticationToken token) {
        if (token instanceof PasswordToken) {
            PasswordToken passwordToken = (PasswordToken) token;
            AccountUser user = accountClient.authByPassword(passwordToken);
            return createUserClaims(user, token);
        } else {
            // 不是我处理的token，返回null继续下个认证
            return null;
        }
    }

    private UserClaims createUserClaims(AccountUser user, AuthenticationToken token) {
        long expiresAt = token.isRememberMe() ?
                (System.currentTimeMillis() + tokenLongExpireMilliseconds) :
                (System.currentTimeMillis() + tokenExpireMilliseconds);

        int type = user.getUserType();
        Long userId = user.getUserId();

        if (type != GlobalConstants.USER_TYPE_EMPLOYEE) {
            throw new BusinessException("当前系统只支持员工类型用户登录");
        }

        Employee employee = employeeService.get(userId);
        long hospitalId = employee.getHospitalId();
        Hospital hospital = hospitalService.get(hospitalId);
        if (hospital == null || !hospital.getIsEnabled()) {
            throw new BusinessException("账号异常，所属诊所不可用");
        }

        return new HisUserClaims(userId, expiresAt, employee.getRoleIds(), employee.getName(), employee.getEmployeeNo(),
                employee.getWorkScope(), hospitalId, hospital.getName(), user.getTenantId());
    }


}
