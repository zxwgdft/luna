package com.luna.tenant.core;

import com.luna.framework.security.UserClaims;
import com.luna.framework.security.UserSession;
import com.luna.framework.security.UserSessionFactory;

public class TenantUserSessionFactory implements UserSessionFactory {

    @Override
    public UserSession createUserSession(UserClaims userClaims) {
        return new TenantUserSession((TenantUserClaims) userClaims);
    }

}
