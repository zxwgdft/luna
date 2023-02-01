package com.luna.constant;

public interface GlobalConstants {

    /**
     * 租户系统管理员
     */
    int USER_TYPE_ADMIN = 99;

    /**
     * 租户
     */
    int USER_TYPE_TENANT = 1;
    /**
     * 员工
     */
    int USER_TYPE_EMPLOYEE = 2;


    // -----------------------------
    // 工作范围
    // -----------------------------

    /**
     * 指定多个医院
     */
    int WORK_SCOPE_HOSPITAL = 1;
    /**
     * 整个公司（租户下所有医院）
     */
    int WORK_SCOPE_TENANT = 9;


}
