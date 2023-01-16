package com.luna.his.patient.service.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@ApiModel
public class PatientSelfDTO {

	@ApiModelProperty("病案号")
	@NotNull(message = "病案号不能为空")
	private Long docNo;

	@ApiModelProperty("头像")
	@NotEmpty(message = "头像不能为空")
	@Length(max = 50, message = "头像长度不能大于50")
	private String profilePhoto;

	@ApiModelProperty("生日")
	private Date birthday;

	@ApiModelProperty("昵称")
	@Length(max = 20, message = "昵称长度不能大于20")
	private String nickName;

	@ApiModelProperty("国籍")
	@Length(max = 20, message = "国籍长度不能大于20")
	private String nationality;

	@ApiModelProperty("身份证")
	@Length(max = 20, message = "身份证长度不能大于20")
	private String idCardNo;

	@ApiModelProperty("发证机关")
	@Length(max = 30, message = "发证机关长度不能大于30")
	private String idCardIssuer;

	@ApiModelProperty("有效期")
	private Date expireDate;

	@ApiModelProperty("血型")
	@Length(max = 10, message = "血型长度不能大于10")
	private String bloodType;

	@ApiModelProperty("籍贯")
	@Length(max = 50, message = "籍贯长度不能大于50")
	private String nativePlace;

	@ApiModelProperty("婚姻")
	private String marriage;

	@ApiModelProperty("职业")
	@Length(max = 30, message = "职业长度不能大于30")
	private String occupation;

	@ApiModelProperty("宗教")
	@Length(max = 20, message = "宗教长度不能大于20")
	private String religion;

	@ApiModelProperty("监护人")
	@Length(max = 20, message = "监护人长度不能大于20")
	private String guardian;

	@ApiModelProperty("其他证件类型")
	private Integer otherIdType;

	@ApiModelProperty("其他证件号码")
	@Length(max = 30, message = "其他证件号码长度不能大于30")
	private String otherIdNo;

	@ApiModelProperty("文化程度")
	private Integer educationLevel;

}