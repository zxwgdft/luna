package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "租户诊所")
public class TenantHospitalDTO {

    @ApiModelProperty("诊所名称")
    @NotEmpty(message = "诊所名称不能为空")
    @Length(max = 50, message = "诊所名称长度不能大于50")
    private String name;

    @ApiModelProperty("租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty("是否总部")
    @NotNull(message = "是否总部不能为空")
    private Boolean isHeadquarter;
}