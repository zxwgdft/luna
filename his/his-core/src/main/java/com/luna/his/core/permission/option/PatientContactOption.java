package com.luna.his.core.permission.option;

/**
 * 患者联系方式查看权限配置
 *
 * @author TontoZhou
 */
public enum PatientContactOption implements BitOption {

    PATIENT_DETAIL("患者详情", 1),
    WORK_TODAY_VIEW("今日工作列表查看", 2),
    WORK_TODAY_EXPORT("今日工作列表导出", 3),
    PATIENT_LIST_VIEW("患者列表查看", 4),
    PATIENT_LIST_EXPORT("患者列表导出", 5),

    ALLOW_MANUAL("允许使用手动方式查看患者手机号和电话", 6),
    ALLOW_VIEW_PATIENT("允许查看患者身份信息", 7),
    ALLOW_LIST_AND_EXPORT("允许查看列表及导出患者身份信息", 8);

    int position;
    String name;

    PatientContactOption(String name, int position) {
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
