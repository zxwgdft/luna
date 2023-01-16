package com.luna.his.core;

import com.luna.framework.security.UserClaims;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
public class HisUserClaims extends UserClaims {
    /**
     * 用户ID
     */
    private long uid;
    /**
     * 用户角色
     */
    private String rid;
    /**
     * 用户名称
     */
    private String unm;
    /**
     * 用户号（工号）
     */
    private String uno;
    /**
     * 用户工作范围
     */
    private int ws;
    /**
     * 当前工作医院ID
     */
    private long hid;
    /**
     * 当前工作医院名称
     */
    private String hnm;
    /**
     * 租户ID
     */
    private long tid;


    public HisUserClaims(long uid, long exp, String rid, String unm, String uno, int ws, long hid, String hnm, long tid) {
        this.uid = uid;
        setExp(exp);
        this.rid = rid;
        this.unm = unm;
        this.uno = uno;
        this.ws = ws;
        this.hid = hid;
        this.hnm = hnm;
        this.tid = tid;
    }

    public HisUserClaims() {

    }

}
