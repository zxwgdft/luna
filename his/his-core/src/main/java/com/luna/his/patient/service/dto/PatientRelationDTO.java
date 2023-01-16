package com.luna.his.patient.service.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@ApiModel
public class PatientRelationDTO {

	@ApiModelProperty("")
	@NotNull(message = "不能为空")
	private Long docNo;

	@ApiModelProperty("责任医生ID")
	@NotNull(message = "责任医生ID不能为空")
	private Long doctorId;

	@ApiModelProperty("开发人员ID")
	@NotNull(message = "开发人员ID不能为空")
	private Long developerId;


	@ApiModelProperty("咨询师ID")
	@NotNull(message = "咨询师ID不能为空")
	private Long consultantId;


	@ApiModelProperty("网电咨询师ID")
	@NotNull(message = "网电咨询师ID不能为空")
	private Long onlineConsultantId;


	@ApiModelProperty("客服ID")
	@NotNull(message = "客服ID不能为空")
	private Long attendantId;

	@ApiModelProperty("顾问ID")
	@NotNull(message = "顾问ID不能为空")
	private Long advisorId;


}