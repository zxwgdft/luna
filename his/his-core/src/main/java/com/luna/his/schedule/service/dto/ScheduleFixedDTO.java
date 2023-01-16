package com.luna.his.schedule.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class ScheduleFixedDTO {

    @ApiModelProperty("员工ID")
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;

    @ApiModelProperty("周几")
    @NotNull(message = "周几不能为空")
    @Max(value = 7, message = "周几数据非法")
    @Min(value = 1, message = "周几数据非法")
    private Integer weekday;

    @ApiModelProperty("诊所ID")
    @NotNull(message = "诊所不能为空")
    private Long hospitalId;

    @ApiModelProperty("是否休息")
    @NotNull(message = "是否休息不能为空")
    private Boolean isRest;

    @ApiModelProperty("是否正常班")
    @NotNull(message = "是否正常班不能为空")
    private Boolean isNormal;

    @ApiModelProperty("班次1")
    private Long shift1;

    @ApiModelProperty("班次2")
    private Long shift2;

}