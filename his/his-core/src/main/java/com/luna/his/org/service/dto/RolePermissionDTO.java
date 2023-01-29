package com.luna.his.org.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

/**
 * @author TontoZhou
 */
@Data
public class RolePermissionDTO {

    @NotNull(message = "角色不能为空")
    private Long id;
    @NotNull(message = "数据查看等级不能为空")
    private Integer dataLevel;
    // 是否我负责的
    private Boolean isInMyCharge;
    // 是否预约过我的
    private Boolean isVisitedMe;
    // 允许查看多少天内报表
    private Integer reportDayLimit;
    // 工作台类型（对应岗位值）
    @NotNull(message = "我的工作不能为空")
    private Integer workspace;
    // 角色权限code集合
    private Set<String> codes;

    private Map<String, Map<String, Boolean>> optionValues;
}
