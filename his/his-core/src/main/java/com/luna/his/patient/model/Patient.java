package com.luna.his.patient.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.framework.service.annotation.SimpleViewField;
import com.luna.his.core.BaseModel;
import com.luna.his.core.log.FieldType;
import com.luna.his.core.log.LogField;
import com.luna.his.core.log.converter.PatientSourceConverter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "病人基础信息")
@TableName("patient")
public class Patient extends BaseModel {

    @ApiModelProperty("病人ID")
    @TableId(type = IdType.NONE)
    @SimpleViewField
    private Long id;

    @ApiModelProperty("患者类型")
    @LogField("患者类型")
    private String type;

    @ApiModelProperty("姓名")
    @SimpleViewField
    @LogField("姓名")
    private String name;

    @ApiModelProperty("性别")
    @SimpleViewField
    @LogField("性别")
    private String sex;

    @ApiModelProperty("来源类型")
    @LogField(name = "来源类型", converter = PatientSourceConverter.class)
    private Integer sourceType;

    @ApiModelProperty("一级来源")
    private Integer srcOtherLevel1;

    @ApiModelProperty("二级来源")
    private Integer srcOtherLevel2;

    @ApiModelProperty("三级来源")
    private Integer srcOtherLevel3;

    @ApiModelProperty("员工介绍人ID")
    private Long srcEmployeeId;

    @ApiModelProperty("患者介绍人ID")
    private Long srcPatientId;

    @ApiModelProperty("创建医院ID")
    @LogField(name = "诊所名称", fieldType = FieldType.HOSPITAL)
    private Long createHospitalId;

    @ApiModelProperty("创建人ID")
    private Long createById;

}