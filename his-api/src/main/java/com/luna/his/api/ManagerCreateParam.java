package com.luna.his.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户管理员创建参数
 *
 * @author TontoZhou
 */
@Data
public class ManagerCreateParam {

    @ApiModelProperty("管理员所属诊所")
    private Long hospitalId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("管理员姓名")
    private String name;

    @ApiModelProperty("管理员账号")
    private String account;

    @ApiModelProperty("管理员手机")
    private String cellphone;

    @ApiModelProperty("管理员密码")
    private String password;


}
