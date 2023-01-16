package com.luna.his.core.constant;

public enum VisitState implements IntCodeEnum {

    /**
     * 0:未确认， 1：已确认，2：已挂号，3：治疗中，4：治疗完成，5：已结账，6：已离开，7：已失约，8：已取消
     */
    NOT_CONFIRMED(0, "未确认"),
    CONFIRMED(1, "已确认"),
    REGISTER(2, "已挂号"),
    TREATMENT(3, "治疗中"),
    TREATMENT_COMPLETE(4, "治疗完成"),
    CHECKOUT(5, "已结账"),
    LEAVE(6, "已离开"),
    MISS(7, "已失约"),
    CANCEL(8, "已取消"),
    ;

    int code;
    String name;

    VisitState(int code, String name) {
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

    public static VisitState of(int code) {
        for (VisitState state : values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
    }
}
