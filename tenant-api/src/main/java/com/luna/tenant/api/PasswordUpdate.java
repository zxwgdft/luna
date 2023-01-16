package com.luna.tenant.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "密码更新")
public class PasswordUpdate {

    @ApiModelProperty("原密码")
    private String oldPassword;
    @ApiModelProperty("新密码")
    private String newPassword;
    @ApiModelProperty("关联人员ID")
    private Long userId;
    @ApiModelProperty("账号类型")
    private Integer type;

}
