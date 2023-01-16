package com.luna.tenant.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TontoZhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "账号用户信息")
public class AccountUser {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("租户ID")
    private Long tenantId;
}
