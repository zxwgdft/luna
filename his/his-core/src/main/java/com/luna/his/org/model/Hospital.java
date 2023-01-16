package com.luna.his.org.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.framework.service.annotation.SimpleViewField;
import com.luna.his.core.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "医院")
@TableName("org_hospital")
public class Hospital extends BaseModel {

    @ApiModelProperty("医院id")
    @TableId(type = IdType.NONE)
    @SimpleViewField
    private Long id;

    @ApiModelProperty("诊所名称")
    @SimpleViewField
    private String name;

    @ApiModelProperty("诊所简称")
    private String shortName;

    @ApiModelProperty("诊所编码")
    private String code;

    @ApiModelProperty("开始营业时间")
    @SimpleViewField
    private Integer openTime;

    @ApiModelProperty("结束营业时间")
    @SimpleViewField
    private Integer closeTime;

    @ApiModelProperty("一周中休息的天")
    @SimpleViewField
    private String restWeekdays;

    @ApiModelProperty("诊所电话")
    private String phoneNum;

    @ApiModelProperty("联系人")
    private String contactName;

    @ApiModelProperty("联系电话")
    private String contactPhoneNum;

    @ApiModelProperty("传真")
    private String faxNumber;

    @ApiModelProperty("省")
    private Integer province;

    @ApiModelProperty("市")
    private Integer city;

    @ApiModelProperty("区")
    private Integer district;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮政编码")
    private String postalCode;

    @ApiModelProperty("网址")
    private String website;

    @ApiModelProperty("图片")
    private String imageId;

    @ApiModelProperty("是否启用")
    @SimpleViewField
    private Boolean isEnabled;

}