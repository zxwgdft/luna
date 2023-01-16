package com.luna.his.patient.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.his.core.BaseModel;
import com.luna.his.core.log.LogField;
import com.luna.his.core.log.converter.PatientMobileConverter;
import com.luna.his.core.log.converter.PatientPhoneConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "病人联系方式")
@TableName("patient_contact")
public class PatientContact extends BaseModel {

    @ApiModelProperty("病人ID")
    @TableId
    private Long id;

    @ApiModelProperty("邮箱")
    @LogField("邮箱")
    private String email;

    @ApiModelProperty("微信")
    @LogField("微信")
    private String weixin;

    @ApiModelProperty("QQ")
    @LogField("QQ")
    private String qq;

    @ApiModelProperty("手机1")
    @LogField(name = "手机", converter = PatientMobileConverter.class)
    private String mobile1;

    @ApiModelProperty("手机1属性")
    private String mobile1re;

    @ApiModelProperty("手机2")
    private String mobile2;

    @ApiModelProperty("手机2属性")
    private String mobile2re;

    @ApiModelProperty("手机3")
    private String mobile3;

    @ApiModelProperty("手机3属性")
    private String mobile3re;

    @ApiModelProperty("电话1")
    @LogField(name = "电话", converter = PatientPhoneConverter.class)
    private String phone1;

    @ApiModelProperty("电话1属性")
    private String phone1re;

    @ApiModelProperty("电话2")
    private String phone2;

    @ApiModelProperty("电话2属性")
    private String phone2re;

    @ApiModelProperty("省")
    private Long province;

    @ApiModelProperty("市")
    private Long city;

    @ApiModelProperty("区")
    private Long district;

    @ApiModelProperty("地址")
    private String address;

}