package com.luna.tenant.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel(description = "登录结果返回")
public class LoginResult {

    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("服务名")
    private String server;
    @ApiModelProperty("用户类型")
    private int userType;

}