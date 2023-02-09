package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "登录用户信息")
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    @ApiModelProperty("用户姓名")
    private String username;

    @ApiModelProperty("权限代码")
    private Collection<String> permissionCodes;

    @ApiModelProperty("是否管理员")
    private Boolean isAdmin;

}
