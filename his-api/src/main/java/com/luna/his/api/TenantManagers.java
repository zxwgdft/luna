package com.luna.his.api;

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
@ApiModel(description = "租户管理员数据")
@AllArgsConstructor
@NoArgsConstructor
public class TenantManagers {
    @ApiModelProperty("具体数据")
    private List<TenantManager> data;
}
