package com.luna.his.org.service.dto;

import com.luna.framework.api.PageParam;
import com.luna.framework.service.QueryType;
import com.luna.framework.service.annotation.QueryCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "员工查询条件")
public class EmployeeQuery extends PageParam {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("员工号")
    @QueryCondition(type = QueryType.LIKE)
    private String employeeNo;

    @ApiModelProperty("性别")
    @QueryCondition(type = QueryType.EQUAL)
    private String sex;

    @ApiModelProperty("岗位")
    @QueryCondition(type = QueryType.EQUAL)
    private Integer job;

    @ApiModelProperty("手机号码")
    @QueryCondition(type = QueryType.LIKE)
    private String cellphone;

    @ApiModelProperty("身份证号")
    @QueryCondition(type = QueryType.LIKE)
    private String idCardNo;

    @ApiModelProperty("是否离职")
    @QueryCondition(type = QueryType.EQUAL)
    private Boolean isLeave;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("账号")
    @QueryCondition(type = QueryType.LIKE)
    private String account;

}