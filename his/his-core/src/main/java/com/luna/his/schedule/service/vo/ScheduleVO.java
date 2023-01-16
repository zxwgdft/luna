package com.luna.his.schedule.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "排班")
public class ScheduleVO {

    @ApiModelProperty("排班ID")
    private Long id;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("医院ID")
    private Long hospitalId;

    @ApiModelProperty("排班日期")
    private Date scheduleDate;

    @ApiModelProperty("排班所属星期几")
    private Integer weekday;

    @ApiModelProperty("几号，月中第几天")
    private Integer day;

    @ApiModelProperty("是否休息")
    private Boolean isRest;

    @ApiModelProperty("是否正常班")
    private Boolean isNormal;

    @ApiModelProperty("班次1")
    private Long shift1;

    @ApiModelProperty("班次2")
    private Long shift2;

}