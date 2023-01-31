package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "租户更新信息")
public class TenantUpdateDTO {

    @ApiModelProperty("租户ID")
    @NotNull(message = "租户不能为空")
    private Long id;

    @ApiModelProperty("租户名称")
    @NotEmpty(message = "租户名称不能为空")
    @Length(max = 50, message = "租户名称长度不能大于50")
    private String name;

    @ApiModelProperty("到期日期")
    @NotNull(message = "到期日期不能为空")
    private Date expireDate;

}