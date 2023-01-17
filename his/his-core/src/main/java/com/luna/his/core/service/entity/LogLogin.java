package com.luna.his.core.service.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "登录日志")
public class LogLogin {
    
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户号（工号）")
    private String userNo;

    @ApiModelProperty("平台")
    private String platform;

    @ApiModelProperty("登录医院、诊所ID")
    private Long hospitalId;

    @ApiModelProperty("登录医院、诊所名称")
    private String hospitalName;

    @ApiModelProperty("设备标识")
    private String deviceId;

    @ApiModelProperty("设备型号")
    private String deviceCode;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("ip属地")
    private String ipLocation;

    @ApiModelProperty("操作时间")
    private Date operateTime;

    @ApiModelProperty("操作")
    private String action;

    @ApiModelProperty("结果")
    private Integer result;

    @ApiModelProperty("备注说明")
    private String remark;

    @ApiModelProperty("租户ID")
    private Long tenantId;
}
