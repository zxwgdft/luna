package com.luna.his.core.permission.option;

/**
 * @author TontoZhou
 */
public interface WorkspaceReceptionist {

    DashboardOption[] dashboardOptions =
            new DashboardOption[]{
                    DashboardOption.APPOINT_ADD,
                    DashboardOption.APPOINT_ADD,
                    DashboardOption.VISIT_TODAY,
                    DashboardOption.INCOME_REAL,
                    DashboardOption.FOLLOW_UP_TIMES,
                    DashboardOption.SHORT_MESSAGE_SEND
            };


    WorkListOption[] workListOptions =
            new WorkListOption[]{
                    WorkListOption.VISIT_TODAY,
                    WorkListOption.BILL_CHECK_TODAY,
                    WorkListOption.FEE_WAITING,
                    WorkListOption.FEE_GOT,
                    WorkListOption.FOLLOW_UP_TODAY,
                    WorkListOption.APPOINT_TOMORROW,
                    WorkListOption.APPOINT_CANCEL,
                    WorkListOption.APPOINT_UNCERTAIN,
                    WorkListOption.REMIND_BIRTHDAY,
                    WorkListOption.APPOINT_ONLINE,
                    WorkListOption.EXPIRE_WARNING,
                    WorkListOption.RETURN_TODAY,
                    WorkListOption.EXPORT_LIST
            };


    VisitOperateOption[] visitOperateOptions = null;
}
