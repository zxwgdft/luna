package com.luna.his.core.log;

/**
 * 日志相关常量
 *
 * @author TontoZhou
 */
public interface LogConstants {

    String PLATFORM_SAAS = "saas";

    // --------------------------------
    // 登录日志相关
    // --------------------------------

    String LOGIN_ACTION_LOGIN = "login";
    String LOGIN_ACTION_LOGOUT = "logout";
    int LOGIN_RESULT_SUCCESS = 1;
    int LOGIN_RESULT_FAIL = 2;

    // --------------------------------
    // 操作日志相关
    // --------------------------------

    // 新增操作
    int OPERATE_TYPE_ADD = 1;
    // 修改操作
    int OPERATE_TYPE_UPDATE = 2;
    // 删除操作
    int OPERATE_TYPE_DELETE = 3;
    // 查看操作
    int OPERATE_TYPE_VIEW = 4;
    // 导出操作
    int OPERATE_TYPE_EXPORT = 5;
    // 合并操作
    int OPERATE_TYPE_MERGE = 6;

}
