package com.luna.his.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户诊所医院创建参数
 *
 * @author TontoZhou
 */
@Data
public class HospitalCreateParam {

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("创建医院ID")
    private Long hospitalId;

    @ApiModelProperty("创建医院名称")
    private String hospitalName;

    @ApiModelProperty("开始营业时间")
    private Integer openTime;

    @ApiModelProperty("停止营业时间")
    private Integer closeTime;

    @ApiModelProperty("休息日")
    private String restWeekdays;

}
