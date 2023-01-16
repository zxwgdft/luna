package com.luna.his.core.permission.option;

/**
 * 今日工作看板配置
 *
 * @author TontoZhou
 */
public enum WorkListOption implements BitOption {

    VISIT_TODAY("今日就诊", 1),
    BILL_CHECK_TODAY("今日对账", 2),
    FEE_WAITING("待收费", 3),
    FEE_GOT("已收费", 4),
    FOLLOW_UP_TODAY("今日随访", 5),
    APPOINT_TODAY("今日预约", 6),
    APPOINT_TOMORROW("明日预约", 7),
    APPOINT_CANCEL("预约已取消", 8),
    APPOINT_UNCERTAIN("待定预约", 9),
    REMIND_BIRTHDAY("生日提醒", 10),
    APPOINT_ONLINE("网络预约", 11),
    EXPIRE_WARNING("有效期预警", 12),
    RETURN_TODAY("今日回访", 13),
    EXPORT_LIST("导出列表", 14);

    int position;
    String name;

    WorkListOption(String name, int position) {
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
