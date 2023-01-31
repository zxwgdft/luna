package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@ApiModel(description = "租户信息")
public class TenantDTO {

    @ApiModelProperty("租户名称")
    @NotEmpty(message = "租户名称不能为空")
    @Size(max = 50, message = "租户名称长度不能大于50")
    private String name;

    @ApiModelProperty("服务器")
    @NotEmpty(message = "服务器不能为空")
    @Size(max = 20, message = "服务器长度不能大于20")
    private String server;

    @ApiModelProperty("到期日期")
    @NotNull(message = "到期日期不能为空")
    private Date expireDate;

}