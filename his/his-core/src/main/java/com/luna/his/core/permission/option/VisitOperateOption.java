package com.luna.his.core.permission.option;

/**
 * 今日工作看板配置
 *
 * @author TontoZhou
 */
public enum VisitOperateOption implements BitOption {

    VISIT_RECEIVE("接诊", 1),
    CHECK("检查", 2),
    VISIT_TRANSFER("转诊", 3),
    CHARGE("已收费", 4),
    DOCTOR_ADVICE("医嘱", 5),
    MEDICAL_RECORD("病历", 6),
    TREAT_PLAN("治疗计划", 7),
    DOCUMENT("文档", 8),
    APPOINT("预约", 9),
    FOLLOW_UP("随访", 10),
    TREAT_FINISH("治疗完成", 11),
    USE_INSTRUMENT("器械使用", 14);

    int position;
    String name;

    VisitOperateOption(String name, int position) {
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
