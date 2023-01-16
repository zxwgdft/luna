package com.luna.his.core.permission.option;

import com.luna.his.core.constant.EmployeeJob;

/**
 * @author TontoZhou
 */
public enum Workspace {

    // 前台工作空间
    RECEPTIONIST(
            EmployeeJob.RECEPTIONIST,
            WorkspaceReceptionist.dashboardOptions,
            WorkspaceReceptionist.workListOptions,
            WorkspaceReceptionist.visitOperateOptions
    ),

    // 医生
    DOCTOR(
            EmployeeJob.DOCTOR,
            WorkspaceDoctor.dashboardOptions,
            WorkspaceDoctor.workListOptions,
            WorkspaceDoctor.visitOperateOptions
    ),

    // 医生助理
    DOCTOR_ASSISTANT(
            EmployeeJob.DOCTOR_ASSISTANT,
            WorkspaceDoctorAssistant.dashboardOptions,
            WorkspaceDoctorAssistant.workListOptions,
            WorkspaceDoctorAssistant.visitOperateOptions
    ),

    // 咨询师
    CONSULTANT(
            EmployeeJob.CONSULTANT,
            WorkspaceConsultant.dashboardOptions,
            WorkspaceConsultant.workListOptions,
            WorkspaceConsultant.visitOperateOptions
    );

    EmployeeJob job;
    DashboardOption[] dashboardOptions;
    WorkListOption[] workListOptions;
    VisitOperateOption[] visitOperateOptions;

    Workspace(EmployeeJob job, DashboardOption[] dashboardOptions, WorkListOption[] workListOptions,
              VisitOperateOption[] visitOperateOptions) {
        this.job = job;
        this.dashboardOptions = dashboardOptions;
        this.workListOptions = workListOptions;
        this.visitOperateOptions = visitOperateOptions;
    }

    public static Workspace getWorkspaceByCode(int code) {
        for (Workspace workspace : Workspace.values()) {
            if (workspace.job.getCode() == code) {
                return workspace;
            }
        }
        return null;
    }

    public EmployeeJob getJob() {
        return job;
    }

    public DashboardOption[] getDashboardOptions() {
        return dashboardOptions;
    }

    public WorkListOption[] getWorkListOptions() {
        return workListOptions;
    }

    public VisitOperateOption[] getVisitOperateOptions() {
        return visitOperateOptions;
    }


}
