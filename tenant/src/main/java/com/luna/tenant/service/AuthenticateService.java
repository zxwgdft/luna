package com.luna.tenant.service;

import com.luna.constant.GlobalConstants;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.WebUtil;
import com.luna.framework.utils.secure.SecureUtil;
import com.luna.his.api.AuthenticatedUser;
import com.luna.his.api.InternalRequestPath;
import com.luna.tenant.api.LoginResult;
import com.luna.tenant.api.PasswordToken;
import com.luna.tenant.core.TenantUserSession;
import com.luna.tenant.model.Account;
import com.luna.tenant.model.Tenant;
import com.luna.tenant.service.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TontoZhou
 */
@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final DynamicHisServlet dynamicHisServlet;
    private final WebSecurityManager webSecurityManager;
    private final AccountService accountService;
    private final TenantService tenantService;

    /**
     * 通过密码登录认证
     *
     * @param passwordToken 账户密码凭票
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public LoginResult loginByPassword(PasswordToken passwordToken, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Account account = authAccountByPassword(passwordToken);
        int type = account.getType();
        if (GlobalConstants.USER_TYPE_EMPLOYEE == type) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setEmployeeId(account.getUserId());
            authenticatedUser.setRememberMe(passwordToken.isRememberMe());
            // TODO ip是否还需要验证
            authenticatedUser.setIp(WebUtil.getIpAddress(WebSecurityManager.getCurrentRequest()));
            authenticatedUser.setDeviceId("");
            authenticatedUser.setDeviceCode("");

            // 获取员工账号的token
            String token = dynamicHisServlet.postJsonRequest(account.getServer(), InternalRequestPath.GET_USER_TOKEN
                    , authenticatedUser, String.class);

            return new LoginResult(token, account.getServer(), account.getType());
        } else if (GlobalConstants.USER_TYPE_TENANT == type || GlobalConstants.USER_TYPE_ADMIN == type) {
            String token = webSecurityManager.authorize(passwordToken, request, response);
            return new LoginResult(token, account.getServer(), type);
        } else {
            // 其他账号用户还未实现
            throw new BusinessException("暂时不支持的账号用户");
        }
    }

    /**
     * 通过账号密码认证
     *
     * @param passwordToken
     * @return
     */
    public Account authAccountByPassword(PasswordToken passwordToken) {
        String username = passwordToken.getUsername();
        if (StringUtil.isNotEmpty(username)) {
            Account account = accountService.getAccountByAccount(username);
            if (account != null) {
                String password = passwordToken.getPassword();
                if (StringUtil.isNotEmpty(password) &&
                        StringUtil.equals(
                                SecureUtil.hashByMD5(password, account.getSalt()),
                                account.getPassword()
                        )
                ) {
                    if (account.getState() != Account.STATE_ENABLED) {
                        throw new BusinessException("账号已被锁定，请联系管理员解决！");
                    }

                    if (account.getType() != GlobalConstants.USER_TYPE_ADMIN) {
                        Tenant tenant = tenantService.get(account.getTenantId());
                        if (!tenant.getIsEnabled()) {
                            throw new BusinessException("当前租户被锁或已经过期，请联系管理员解决！");
                        }
                    }

                    // 认证成功
                    return account;
                }
            }
        }
        throw new BusinessException("账号或密码错误！");
    }

    public LoginUser getCurrentUserInfo() {
        TenantUserSession userSession = (TenantUserSession) WebSecurityManager.getCurrentUserSession();
        if (userSession.getUserType() == GlobalConstants.USER_TYPE_ADMIN) {
            return new LoginUser(userSession.getUserName(), null, true);
        } else {
            throw new RuntimeException("还未开发完成的用户");
        }
    }
}
