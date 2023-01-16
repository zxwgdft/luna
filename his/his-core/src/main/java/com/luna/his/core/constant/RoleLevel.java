package com.luna.his.core.constant;

/**
 * 角色数据权限等级
 *
 * @author TontoZhou
 */
public enum RoleLevel implements IntCodeEnum {

    TENANT(9, "全部"),
    HOSPITAL(3, "诊所"),
    DEPARTMENT(2, "科室"),
    SELF(1, "个人");

    int code;
    String name;

    RoleLevel(int code, String name) {
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
