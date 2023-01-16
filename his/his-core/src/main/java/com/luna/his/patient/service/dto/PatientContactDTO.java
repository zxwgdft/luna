package com.luna.his.patient.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter 
@Setter
@ToString
@ApiModel
public class PatientContactDTO {

	@ApiModelProperty("邮箱")
	@Length(max = 50, message = "邮箱长度不能大于50")
	private String email;

	@ApiModelProperty("微信")
	@Length(max = 20, message = "微信长度不能大于20")
	private String weixin;

	@ApiModelProperty("QQ")
	@Length(max = 15, message = "QQ长度不能大于15")
	private String qq;

	@ApiModelProperty("手机1")
	@Length(max = 15, message = "手机1长度不能大于15")
	private String mobile1;

	@ApiModelProperty("手机1属性")
	@Length(max = 5, message = "手机1属性长度不能大于5")
	private String mobile1re;

	@ApiModelProperty("手机2")
	@Length(max = 15, message = "手机2长度不能大于15")
	private String mobile2;

	@ApiModelProperty("手机2属性")
	@Length(max = 5, message = "手机2属性长度不能大于5")
	private String mobile2re;

	@ApiModelProperty("手机3")
	@Length(max = 15, message = "手机3长度不能大于15")
	private String mobile3;

	@ApiModelProperty("手机3属性")
	@Length(max = 5, message = "手机3属性长度不能大于5")
	private String mobile3re;

	@ApiModelProperty("电话1")
	@Length(max = 15, message = "电话1长度不能大于15")
	private String phone1;

	@ApiModelProperty("电话1属性")
	@Length(max = 5, message = "电话1属性长度不能大于5")
	private String phone1re;

	@ApiModelProperty("电话2")
	@Length(max = 15, message = "电话2长度不能大于15")
	private String phone2;

	@ApiModelProperty("电话2属性")
	@Length(max = 5, message = "电话2属性长度不能大于5")
	private String phone2re;

	@ApiModelProperty("省")
	private Long province;

	@ApiModelProperty("市")
	private Long city;

	@ApiModelProperty("区")
	private Long district;

	@ApiModelProperty("地址")
	@Length(max = 200, message = "地址长度不能大于200")
	private String address;

}