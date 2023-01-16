package com.luna.his.core.log;

/**
 * @author TontoZhou
 */
public enum Module {

    //
    PATIENT_MANAGE("患者维护"),
    PATIENT_VIEW("患者查看"),
    VISIT_MANAGE("预约管理"),
    EMPLOYEE_MANAGE("员工管理"),
    MEMBER_MANAGE("会员管理"),
    FOLLOW_UP("随访");

    String _name;

    Module(String name) {
        this._name = name;
    }
}
