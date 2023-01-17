package com.luna.his.core.service.entity;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "操作日志")
public class LogOperate {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户号（工号）")
    private String userNo;

    @ApiModelProperty("患者")
    private String patientName;

    @ApiModelProperty("平台")
    private String platform;

    @ApiModelProperty("登录医院、诊所ID")
    private Long hospitalId;

    @ApiModelProperty("登录医院、诊所名称")
    private String hospitalName;

    @ApiModelProperty("操作时间")
    private Date operateTime;

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("操作类型")
    private String actionType;

    @ApiModelProperty("操作")
    private String action;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("变更内容")
    private String content;
}
