package com.luna.his.core.service.dto;

import com.luna.his.org.model.Hospital;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "登录用户信息")
public class LoginUser {

    @ApiModelProperty("用户姓名")
    private String username;

    @ApiModelProperty("工作诊所")
    private List<Hospital> workHospitals;

    @ApiModelProperty("当前工作诊所Id")
    private Long curHospitalId;

    @ApiModelProperty("权限代码")
    private Collection<String> permissionCodes;

    @ApiModelProperty("是否管理员")
    private Boolean isAdmin;

}
