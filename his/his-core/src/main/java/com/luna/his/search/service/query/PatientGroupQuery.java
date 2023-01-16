package com.luna.his.search.service.query;

import com.luna.framework.api.PageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "患者分组查询条件")
public class PatientGroupQuery extends PageParam {

    @ApiModelProperty("分组ID")
    private Long groupId;

}
