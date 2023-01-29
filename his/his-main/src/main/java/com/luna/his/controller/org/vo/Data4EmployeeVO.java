package com.luna.his.controller.org.vo;

import com.luna.his.org.model.Hospital;
import com.luna.his.org.model.OrgRole;
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
    private List<OrgRole> roles;

    @ApiModelProperty("所有医院")
    private List<Hospital> hospitals;

}
