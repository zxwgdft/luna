package com.luna.his.controller.org.vo;

import com.luna.his.org.model.Hospital;
import com.luna.his.sys.model.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TontoZhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "员工需要的数据")
public class Data4EmployeeVO {

    @ApiModelProperty("所有角色")
    private List<SysRole> roles;

    @ApiModelProperty("所有科室")
    private List<Department> departments;

    @ApiModelProperty("所有医院")
    private List<Hospital> hospitals;

}
