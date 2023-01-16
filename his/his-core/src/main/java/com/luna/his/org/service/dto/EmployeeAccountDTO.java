package com.luna.his.org.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "员工创建账号信息")
public class EmployeeAccountDTO {

    @ApiModelProperty("员工ID")
    @NotNull(message = "员工ID不能为空")
    private Long id;

    @ApiModelProperty("用户账户")
    @Size(max = 30, message = "用户账户长度不能大于30")
    @NotEmpty(message = "用户账号不能为空")
    private String account;

    @ApiModelProperty("用户密码")
    @Size(max = 30, message = "用户密码长度不能大于30")
    private String password;

    @ApiModelProperty("手机号码")
    @Size(max = 15, message = "手机号码长度不能大于15")
    private String cellphone;

    @ApiModelProperty("角色")
    @NotNull(message = "角色不能为空")
    @Size(min = 1, message = "角色不能为空")
    private List<Long> roleIds;

}