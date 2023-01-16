package com.luna.his.core.permission.option;

/**
 * 报表配置
 *
 * @author TontoZhou
 */
public enum ReportOption implements BitOption {

    VIEW_CHARGE_AMOUNT("允许查看医生收费明细中金额字段", 1);

    int position;
    String name;

    ReportOption(String name, int position) {
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
