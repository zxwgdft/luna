package com.luna.his.core;

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
public class HisUserSession implements UserSession<Long> {

    private long userId;
    private String userName;

    @Getter
    private String userNo;
    @Getter
    private long hospitalId;
    @Getter
    private String hospitalName;
    @Getter
    private long tenantId;
    @Getter
    private int workScope;
    @Getter
    private Set<Long> roleIds;

    public HisUserSession(HisUserClaims userClaims) {
        this.userId = userClaims.getUid();
        this.userName = userClaims.getUnm();
        this.roleIds = parseRole(userClaims.getRid());
        this.userNo = userClaims.getUno();
        this.hospitalId = userClaims.getHid();
        this.hospitalName = userClaims.getHnm();
        this.tenantId = userClaims.getTid();
        this.workScope = userClaims.getWs();
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    private Set<Long> parseRole(String roleString) {
        if (roleString != null && roleString.length() > 0) {
            Set<Long> roleIds = new HashSet<>();
            for (String r : roleString.split(",")) {
                if (r.length() > 0) {
                    long rid = Long.valueOf(r);
                    roleIds.add(rid);
                }
            }
            return roleIds;
        }
        return Collections.EMPTY_SET;
    }
}
