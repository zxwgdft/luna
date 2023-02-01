package com.luna.his.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "租户管理员")
public class TenantManager  {

    @ApiModelProperty("员工ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号码")
    private String cellphone;

    @ApiModelProperty("账号")
    private String account;

}