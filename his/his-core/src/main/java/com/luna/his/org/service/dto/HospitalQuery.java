package com.luna.his.org.service.dto;

import com.luna.framework.api.PageParam;
import com.luna.framework.service.QueryType;
import com.luna.framework.service.annotation.QueryCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HospitalQuery extends PageParam {

    @ApiModelProperty("医院名称")
    @QueryCondition(type = QueryType.LIKE)
    private String name;

    @ApiModelProperty("是否启用")
    @QueryCondition(type = QueryType.EQUAL)
    private Boolean isEnabled;

}