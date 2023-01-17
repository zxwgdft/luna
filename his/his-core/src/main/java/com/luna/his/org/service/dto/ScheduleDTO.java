package com.luna.his.org.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class ScheduleDTO {

    @ApiModelProperty("员工ID")
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;

    @ApiModelProperty("排班时间")
    @NotNull(message = "排班时间不能为空")
    private Date scheduleDate;

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