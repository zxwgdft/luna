package com.luna.his.core.constant;

/**
 * 员工岗位
 *
 * @author TontoZhou
 */
public enum EmployeeJob implements IntCodeEnum {

    MANAGER(0, "诊所管理员"),
    DOCTOR(1, "医生"),
    NURSE(2, "护士"),
    RECEPTIONIST(3, "前台"),
    TECHNICIAN(4, "技师"),
    ADMINISTRATIVE(5, "行政"),
    FINANCIAL(6, "财务"),
    DOCTOR_ASSISTANT(7, "牙医助理"),
    CONSULTANT(8, "咨询师"),
    ATTENDANT(9, "客服"),
    ONLINE_CONSULTANT(10, "网电营销"),
    INVENTORY_MANAGER(11, "库管"),
    BUYER(12, "采购"),
    CHECK_DOCTOR(13, "检查医师");

    int code;
    String name;

    EmployeeJob(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

}
