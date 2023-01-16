package com.luna.tenant.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "账号删除")
public class AccountDelete {

    @ApiModelProperty("关联人员ID")
    private Long userId;
    @ApiModelProperty("账号类型")
    private Integer type;

}
