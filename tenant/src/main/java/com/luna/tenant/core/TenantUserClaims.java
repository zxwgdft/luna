package com.luna.tenant.core;

import com.luna.framework.security.UserClaims;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
public class TenantUserClaims extends UserClaims {
    /**
     * 用户ID
     */
    private long uid;
    /**
     * 用户名称
     */
    private String unm;
    /**
     * 用户类型
     */
    private int utp;
    /**
     * 租户ID
     */
    private long tid;
    /**
     * 服务器
     */
    private String svr;


    public TenantUserClaims(long uid, long exp, String unm, int utp, long tid, String svr) {
        this.uid = uid;
        setExp(exp);
        this.unm = unm;
        this.utp = utp;
        this.tid = tid;
        this.svr = svr;
    }

    public TenantUserClaims() {

    }

}
