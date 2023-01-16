package com.luna.his.patient.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.his.core.BaseModel;
import com.luna.his.core.log.LogField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "病人个人信息")
@TableName("patient_self")
public class PatientSelf extends BaseModel {

    @ApiModelProperty("病人ID")
    @TableId
    private Long id;

    @ApiModelProperty("生日")
    @LogField("生日")
    private Date birthday;

    @ApiModelProperty("昵称")
    @LogField("昵称")
    private String nickName;

    @ApiModelProperty("国籍")
    private String nationality;

    @ApiModelProperty("身份证")
    @LogField("身份证号")
    private String idCardNo;

    @ApiModelProperty("发证机关")
    private String idCardIssuer;

    @ApiModelProperty("有效期")
    private Date expireDate;

    @ApiModelProperty("血型")
    private String bloodType;

    @ApiModelProperty("籍贯")
    private String nativePlace;

    @ApiModelProperty("婚姻")
    private String marriage;

    @ApiModelProperty("职业")
    private String occupation;

    @ApiModelProperty("宗教")
    private String religion;

    @ApiModelProperty("监护人")
    private String guardian;

    @ApiModelProperty("其他证件类型")
    private Integer otherIdType;

    @ApiModelProperty("其他证件号码")
    private String otherIdNo;

    @ApiModelProperty("文化程度")
    private Integer educationLevel;

}