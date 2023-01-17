package com.luna.his.core.service.dto;

import com.luna.framework.api.PageParam;
import com.luna.his.core.es.DateRange;
import com.luna.his.core.es.ESQuery;
import com.luna.his.core.es.ESQueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登录日志查询条件")
public class LogOperateQuery extends PageParam {

    @ApiModelProperty("创建时间")
    @ESQuery(type = ESQueryType.BETWEEN)
    private DateRange operateTime;

    @ApiModelProperty("用户ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private String userId;

    @ApiModelProperty("平台")
    @ESQuery(type = ESQueryType.EQUAL)
    private String platform;

    @ApiModelProperty("模块")
    @ESQuery(type = ESQueryType.EQUAL)
    private String module;

    @ApiModelProperty("操作类型")
    @ESQuery(type = ESQueryType.EQUAL)
    private String actionType;

    @ApiModelProperty("租户ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long tenantId;
}