package com.luna.tenant.core;

import com.luna.constant.GlobalConstants;
import com.luna.framework.api.AuthenticationToken;
import com.luna.framework.api.SystemException;
import com.luna.framework.security.Authorizer;
import com.luna.framework.security.UserClaims;
import com.luna.tenant.api.PasswordToken;
import com.luna.tenant.model.Account;
import com.luna.tenant.service.AccountService;
import com.luna.tenant.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class TenantAuthorizer implements Authorizer {

    @Value("${auth.token-expire-milliseconds:3600000}")
    private int tokenExpireMilliseconds;

    @Value("${auth.token-long-expire-milliseconds:3600000}")
    private int tokenLongExpireMilliseconds;

    @Autowired
    private AuthenticateService authenticateService;

    @Override
    public UserClaims authorize(AuthenticationToken token) {
        if (token instanceof PasswordToken) {
            PasswordToken passwordToken = (PasswordToken) token;
            Account account = authenticateService.authAccountByPassword(passwordToken);
            return createUserClaims(account, token);
        } else if (token instanceof AuthenticatedToken) {
            AuthenticatedToken authenticatedToken = (AuthenticatedToken) token;
            return createUserClaims(authenticatedToken.getAccount(), token);
        } else {
            // 不是我处理的token，返回null继续下个认证
            return null;
        }
    }

    private UserClaims createUserClaims(Account account, AuthenticationToken token) {
        // 认证成功
        long expiresAt = token.isRememberMe() ?
                (System.currentTimeMillis() + tokenLongExpireMilliseconds) :
                (System.currentTimeMillis() + tokenExpireMilliseconds);

        int type = account.getType();
        Long tenantId = account.getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }

        if (type != GlobalConstants.USER_TYPE_TENANT && type != GlobalConstants.USER_TYPE_ADMIN) {
            throw new SystemException(SystemException.CODE_ERROR_CODE, "不应该认证的用户类型");
        }

        return new TenantUserClaims(account.getId(), expiresAt, account.getAccount(), account.getType(),
                tenantId, account.getServer());
    }


}
