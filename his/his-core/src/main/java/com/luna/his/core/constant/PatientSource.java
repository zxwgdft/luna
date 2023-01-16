package com.luna.his.core.constant;

/**
 * 员工岗位
 *
 * @author TontoZhou
 */
public enum PatientSource implements IntCodeEnum {

    EXTERNAL(1, "外部来源"),
    FRIEND(2, "朋友介绍"),
    EMPLOYEE(3, "员工介绍");

    int code;
    String name;

    PatientSource(int code, String name) {
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
