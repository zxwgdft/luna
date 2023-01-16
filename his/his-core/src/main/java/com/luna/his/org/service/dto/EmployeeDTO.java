package com.luna.his.org.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel
public class EmployeeDTO {

    @ApiModelProperty("员工ID")
    private Long id;

    @ApiModelProperty("姓名")
    @NotEmpty(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能大于20")
    private String name;

    @ApiModelProperty("员工号")
    @Size(max = 20, message = "员工号不能大于20")
    private String employeeNo;

    @ApiModelProperty("出生日期")
    private Date birthday;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("岗位")
    @NotNull(message = "岗位不能为空")
    private Integer job;

    @ApiModelProperty("是否实习生")
    private Boolean isTrainee;

    @ApiModelProperty("所属医院")
    private Long hospitalId;

    @ApiModelProperty("工作医院")
    @NotNull(message = "工作医院不能为空")
    @Size(min = 1, message = "工作医院不能为空")
    private List<Long> workHospitalIds;

    @ApiModelProperty("手机号码")
    @Size(max = 15, message = "手机号码长度不能大于15")
    private String cellphone;

    @ApiModelProperty("身份证号")
    @Size(max = 20, message = "身份证号长度不能大于20")
    private String idCardNo;

    @ApiModelProperty("是否离职")
    @NotNull(message = "是否离职不能为空")
    private Boolean isLeave;

}