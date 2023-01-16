package com.luna.his.core.permission.option;

/**
 * 今日工作看板配置
 *
 * @author TontoZhou
 */
public enum DashboardOption implements BitOption {

    PATIENT_ADD("新增患者", 1),
    APPOINT_ADD("新增预约", 2),
    APPOINT_ADD_TODAY("今日新增预约", 3),
    VISIT_TODAY("今日就诊", 4),
    VISIT_FIRST_TODAY("今日初诊/总诊", 5),
    RECIPE_TODAY("今日处方总数", 6),
    MEDICAL_RECORD_TODAY("今日病历书写", 7),
    INCOME_REAL("实收金额", 8),
    INCOME_REAL_TODAY("今日实收金额", 9),
    FOLLOW_UP_TIMES("随访人次", 10),
    FOLLOW_UP_TIMES_TODAY("今日实际随访/计划随划", 11),
    SHORT_MESSAGE_SEND("短信发送", 12);

    int position;
    String name;

    DashboardOption(String name, int position) {
        this.position = position;
        this.name = name;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String getName() {
        return name;
    }
}
