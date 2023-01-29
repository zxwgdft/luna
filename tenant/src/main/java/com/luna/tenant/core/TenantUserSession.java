package com.luna.tenant.core;

import com.luna.framework.security.UserClaims;
import com.luna.framework.security.UserSession;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * his用户会话对象
 *
 * @author TontoZhou
 */
public class TenantUserSession implements UserSession<Long> {

    private long userId;
    private String userName;

    @Getter
    private int userType;
    @Getter
    private String server;
    @Getter
    private long tenantId;

    public TenantUserSession(TenantUserClaims userClaims) {
        this.userId = userClaims.getUid();
        this.userName = userClaims.getUnm();
        this.userType = userClaims.getUtp();
        this.tenantId = userClaims.getTid();
        this.server = userClaims.getSvr();
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }


}
