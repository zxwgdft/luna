package com.luna.his.org.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@ApiModel
public class HospitalDTO {

	private Long id;

	@ApiModelProperty("诊所名称")
	@NotEmpty(message = "诊所名称不能为空")
	@Size(max = 50, message = "诊所名称长度不能大于50")
	private String name;

	@ApiModelProperty("诊所简称")
	@Size(max = 50, message = "诊所简称长度不能大于20")
	private String shortName;

	@ApiModelProperty("诊所编码")
	@Size(max = 20, message = "诊所编码长度不能大于20")
	private String code;

	@ApiModelProperty("开始营业时间")
	private Integer openTime;

	@ApiModelProperty("结束营业时间")
	private Integer closeTime;

	@ApiModelProperty("诊所电话")
	@Size(max = 15, message = "诊所电话长度不能大于15")
	private String phoneNum;

	@ApiModelProperty("联系人")
	@Size(max = 20, message = "联系人长度不能大于20")
	private String contactName;

	@ApiModelProperty("联系电话")
	@Size(max = 15, message = "联系电话长度不能大于15")
	private String contactPhoneNum;

	@ApiModelProperty("")
	@Size(max = 20, message = "长度不能大于20")
	private String faxNumber;

	@ApiModelProperty("省")
	private Integer province;

	@ApiModelProperty("市")
	private Integer city;

	@ApiModelProperty("区")
	private Integer district;

	@ApiModelProperty("地址")
	@Size(max = 255, message = "地址长度不能大于255")
	private String address;

	@ApiModelProperty("邮政编码")
	@Size(max = 10, message = "邮政编码长度不能大于10")
	private String postalCode;

	@ApiModelProperty("网址")
	@Size(max = 255, message = "网址长度不能大于255")
	private String website;

	@ApiModelProperty("图片")
	@Size(max = 255, message = "图片id长度不能大于255")
	private String imageId;

	@ApiModelProperty("是否启用")
	@NotNull(message = "是否启用不能为空")
	private Boolean isEnabled;

}