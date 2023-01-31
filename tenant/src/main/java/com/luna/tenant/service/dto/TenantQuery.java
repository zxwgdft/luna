package com.luna.tenant.service.dto;

import com.luna.framework.api.PageParam;
import com.luna.framework.service.QueryType;
import com.luna.framework.service.annotation.QueryCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "租户查询条件")
public class TenantQuery extends PageParam {

    @ApiModelProperty("租户名称")
    @QueryCondition(type = QueryType.LIKE)
    private String name;

    @ApiModelProperty("姓名")
    @QueryCondition(type = QueryType.EQUAL)
    private String server;


}