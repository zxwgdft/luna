package com.luna.his.org.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.his.core.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "排班")
@TableName("org_schedule")
public class Schedule extends BaseModel {

	@ApiModelProperty("排班ID")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("员工ID")
	private Long employeeId;

	@ApiModelProperty("医院ID")
	private Long hospitalId;

	@ApiModelProperty("排班日期")
	private Date scheduleDate;

	@ApiModelProperty("排班日期所在周")
	private Integer week;

	@ApiModelProperty("排班所属星期几")
	private Integer weekday;

	@ApiModelProperty("年月，例如2022年1月=202201")
	private Integer month;

	@ApiModelProperty("几号，某月中第几天")
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