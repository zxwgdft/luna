package com.luna.tenant.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "账号更新信息")
public class AccountUpdate {

    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("手机号")
    private String cellphone;
    @ApiModelProperty("关联人员ID")
    private Long userId;
    @ApiModelProperty("账号类型")
    private Integer type;

}
