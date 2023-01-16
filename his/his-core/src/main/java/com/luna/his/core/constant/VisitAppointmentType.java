package com.luna.his.core.constant;

public enum VisitAppointmentType implements IntCodeEnum {

    /**
     * 1:普通， 2：待定
     */
    COMMON(1, "普通"),
    PENDING(2, "待定");

    int code;
    String name;

    VisitAppointmentType(int code, String name) {
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
