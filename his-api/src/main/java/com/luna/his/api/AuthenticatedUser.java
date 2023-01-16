package com.luna.his.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "已认证的用户")
public class AuthenticatedUser {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("是否记住")
    private boolean rememberMe;

    @ApiModelProperty("请求IP")
    private String ip;

    @ApiModelProperty("设备标识")
    private String deviceId;

    @ApiModelProperty("设备型号")
    private String deviceCode;

}
