package com.luna.his.org.service.vo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 角色
 *
 * @author TontoZhou
 * @since 2022/12/11
 */
@Data
public class RolePermissionVO {

    // ID
    private Long id;
    // 是否系统默认
    private Boolean isDefault;
    // 角色查看等级
    private Integer dataLevel;
    // 是否我负责的
    private Boolean isInMyCharge;
    // 是否预约过我的
    private Boolean isVisitedMe;
    // 允许查看多少天内报表
    private Integer reportDayLimit;
    // 工作台类型（对应岗位值）
    private Integer workspace;
    // 角色权限code集合
    private Set<String> codes;
    // BitOption配置
    private Map<String, Map<String, Boolean>> optionValues;
}
