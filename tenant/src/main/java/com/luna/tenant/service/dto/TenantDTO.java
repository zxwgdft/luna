package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "租户信息")
public class TenantDTO {

    @ApiModelProperty("租户名称")
    @NotEmpty(message = "租户名称不能为空")
    @Length(max = 50, message = "租户名称长度不能大于50")
    private String name;

    @ApiModelProperty("服务器ID")
    @NotNull(message = "服务器不能为空")
    private Long serverId;

    @ApiModelProperty("到期年限")
    @NotNull(message = "到期年限不能为空")
    private Integer expireYears;

}