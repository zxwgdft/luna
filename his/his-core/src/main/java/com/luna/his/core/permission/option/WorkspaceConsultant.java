package com.luna.his.core.permission.option;

/**
 * @author TontoZhou
 */
public interface WorkspaceConsultant {

    DashboardOption[] dashboardOptions = null;


    WorkListOption[] workListOptions =
            new WorkListOption[]{
                    WorkListOption.VISIT_TODAY,
                    WorkListOption.FOLLOW_UP_TODAY,
                    WorkListOption.RETURN_TODAY,
                    WorkListOption.EXPORT_LIST
            };


    VisitOperateOption[] visitOperateOptions = null;
}
