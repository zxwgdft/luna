package com.luna.his.schedule.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.his.core.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "固定排班")
@TableName("schedule_fixed")
public class ScheduleFixed extends BaseModel {

	@ApiModelProperty("固定排班ID")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("医生ID")
	private Long employeeId;

	@ApiModelProperty("医院ID")
	private Long hospitalId;

	@ApiModelProperty("排班所属星期几")
	private Integer weekday;

	@ApiModelProperty("是否休息")
	private Boolean isRest;

	@ApiModelProperty("是否正常班")
	private Boolean isNormal;

	@ApiModelProperty("班次1")
	private Long shift1;

	@ApiModelProperty("班次2")
	private Long shift2;

}