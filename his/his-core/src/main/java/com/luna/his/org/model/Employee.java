package com.luna.his.org.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.framework.service.annotation.SimpleViewField;
import com.luna.his.core.BaseModel;
import com.luna.his.core.constant.EmployeeJob;
import com.luna.his.core.log.converter.IntConstantConverter;
import com.luna.his.core.log.FieldType;
import com.luna.his.core.log.LogField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "医院员工")
@TableName("org_employee")
public class Employee extends BaseModel {

    @ApiModelProperty("员工ID")
    @SimpleViewField
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("姓名")
    @SimpleViewField
    @LogField("姓名")
    private String name;

    @ApiModelProperty("姓名拼音首字母")
    private String namePy;

    @ApiModelProperty("员工号")
    @LogField("员工号")
    @SimpleViewField
    private String employeeNo;

    @ApiModelProperty("出生日期")
    @LogField("出生日期")
    private Date birthday;

    @ApiModelProperty("性别")
    @LogField("性别")
    private String sex;

    @ApiModelProperty("岗位")
    @SimpleViewField
    @LogField(name = "岗位", converter = IntConstantConverter.class, enumType = EmployeeJob.class)
    private Integer job;

    @ApiModelProperty("是否实习生")
    @LogField("是否实习生")
    private Boolean isTrainee;

    @ApiModelProperty("所属医院")
    @LogField(name = "所属医院", fieldType = FieldType.HOSPITAL)
    private Long hospitalId;

    @ApiModelProperty("工作范围")
    private Integer workScope;

    @ApiModelProperty("工作医院")
    @LogField(name = "工作医院", fieldType = FieldType.HOSPITAL, isMulti = true)
    private String workHospitalIds;

    @ApiModelProperty("手机号码")
    @LogField("手机号码")
    private String cellphone;

    @ApiModelProperty("身份证号")
    @LogField("身份证号")
    private String idCardNo;

    @ApiModelProperty("是否离职")
    @LogField("是否离职")
    private Boolean isLeave;

    @ApiModelProperty("角色ID")
    private String roleIds;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("手机是否验证")
    private Boolean isPhoneCertified;

    @ApiModelProperty("是否管理员")
    private Boolean isManager;
}