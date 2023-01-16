package com.luna.his.core;

import com.luna.framework.security.UserClaims;
import com.luna.framework.security.UserSession;
import com.luna.framework.security.UserSessionFactory;

public class HisUserSessionFactory implements UserSessionFactory {

    @Override
    public UserSession createUserSession(UserClaims userClaims) {
        return new HisUserSession((HisUserClaims) userClaims);
    }

}
