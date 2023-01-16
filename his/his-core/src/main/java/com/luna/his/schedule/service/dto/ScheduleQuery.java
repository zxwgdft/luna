package com.luna.his.schedule.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "排班查询条件")
public class ScheduleQuery {

    @ApiModelProperty("查询日期")
    @NotNull(message = "查询日期不能为空")
    private Date date;

    @ApiModelProperty("查询诊所")
    @NotNull(message = "查询诊所不能为空")
    private Long hospitalId;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("查询科室")
    private Long departId;

    @ApiModelProperty("查询岗位")
    private Integer job;

    @ApiModelProperty(hidden = true)
    private Integer week;

    @ApiModelProperty(hidden = true)
    private Integer month;

}