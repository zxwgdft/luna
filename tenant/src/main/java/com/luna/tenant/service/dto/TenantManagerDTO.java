package com.luna.tenant.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 租户管理员创建参数
 *
 * @author TontoZhou
 */
@Data
public class TenantManagerDTO {

    @ApiModelProperty("租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty("管理员姓名")
    @NotEmpty(message = "管理员姓名不能为空")
    @Length(max = 20, message = "管理员姓名长度不能大于20")
    private String name;

    @ApiModelProperty("管理员账号")
    @NotEmpty(message = "管理员账号不能为空")
    @Length(max = 30, message = "管理员账号长度不能大于30")
    private String account;

    @ApiModelProperty("管理员手机")
    @NotEmpty(message = "管理员手机不能为空")
    @Length(max = 15, message = "管理员手机长度不能大于15")
    private String cellphone;

    @ApiModelProperty("管理员密码")
    @NotEmpty(message = "管理员密码不能为空")
    @Length(max = 50, message = "管理员密码长度不能大于50")
    private String password;


}
