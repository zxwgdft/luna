package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "租户诊所")
public class HospitalDTO {

    @ApiModelProperty("诊所名称")
    @NotEmpty(message = "诊所名称不能为空")
    @Size(max = 50, message = "诊所名称长度不能大于50")
    private String name;

    @ApiModelProperty("租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty("开始营业时间")
    @NotNull(message = "开始营业时间不能为空")
    private Integer openTime;

    @ApiModelProperty("停止营业时间")
    @NotNull(message = "停止营业时间不能为空")
    private Integer closeTime;

    @ApiModelProperty("休息日")
    private List<Integer> restWeekdays;

}