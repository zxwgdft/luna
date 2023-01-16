package com.luna.his.core.permission.option;

/**
 * @author TontoZhou
 */
public interface WorkspaceDoctorAssistant {

    DashboardOption[] dashboardOptions =
            new DashboardOption[]{
                    DashboardOption.VISIT_FIRST_TODAY,
                    DashboardOption.APPOINT_ADD_TODAY,
                    DashboardOption.RECIPE_TODAY,
                    DashboardOption.MEDICAL_RECORD_TODAY,
                    DashboardOption.FOLLOW_UP_TIMES_TODAY,
            };


    WorkListOption[] workListOptions =
            new WorkListOption[]{
                    WorkListOption.VISIT_TODAY,
                    WorkListOption.FOLLOW_UP_TODAY,
            };


    VisitOperateOption[] visitOperateOptions =
            new VisitOperateOption[]{
                    VisitOperateOption.VISIT_RECEIVE,
                    VisitOperateOption.CHECK,
                    VisitOperateOption.VISIT_TRANSFER,
                    VisitOperateOption.CHARGE,
                    VisitOperateOption.DOCTOR_ADVICE,
                    VisitOperateOption.MEDICAL_RECORD,
                    VisitOperateOption.TREAT_PLAN,
                    VisitOperateOption.DOCUMENT,
                    VisitOperateOption.APPOINT,
                    VisitOperateOption.FOLLOW_UP,
                    VisitOperateOption.TREAT_FINISH,
                    VisitOperateOption.USE_INSTRUMENT
            };
}
