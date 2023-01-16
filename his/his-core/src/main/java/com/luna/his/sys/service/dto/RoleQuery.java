package com.luna.his.sys.service.dto;

import com.luna.framework.api.PageParam;
import com.luna.framework.service.QueryType;
import com.luna.framework.service.annotation.QueryCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "角色查询条件")
public class RoleQuery extends PageParam {

    @ApiModelProperty("角色名称")
    @QueryCondition(type = QueryType.LIKE)
    private String name;

    @ApiModelProperty("是否系统角色")
    @QueryCondition(type = QueryType.EQUAL)
    private Boolean isDefault;

    @ApiModelProperty("是否管理员")
    @QueryCondition(type = QueryType.EQUAL)
    private Boolean isAdmin;

}