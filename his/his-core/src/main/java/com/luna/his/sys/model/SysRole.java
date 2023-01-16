package com.luna.his.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luna.framework.service.annotation.SimpleViewField;
import com.luna.his.core.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "角色")
public class SysRole extends BaseModel {

    @TableId(type = IdType.AUTO)
    @SimpleViewField
    private Long id;

    @ApiModelProperty("角色名称")
    @SimpleViewField
    private String name;

    @ApiModelProperty("是否系统默认角色")
    @SimpleViewField
    private Boolean isDefault;

    @ApiModelProperty("是否公司管理员")
    @SimpleViewField
    private Boolean isAdmin;

    @ApiModelProperty("权限编码拼接字符串")
    private String permissions;

    @ApiModelProperty("角色查看等级")
    private Integer dataLevel;

    @ApiModelProperty("是否我负责的")
    private Boolean isInMyCharge;

    @ApiModelProperty("是否预约过我的")
    private Boolean isVisitedMe;

    @ApiModelProperty("允许查看多少天内报表")
    private Integer reportDayLimit;

    @ApiModelProperty("报表权限设置")
    private Long reportSet;

    @ApiModelProperty("患者联系方式查看权限设置")
    private Long patientContactSet;

    @ApiModelProperty("库存权限设置")
    private Long warehouseSet;

    @ApiModelProperty("工作台")
    private Integer workspace;

    @ApiModelProperty("工作台设置")
    private Long dashboardSet;

    @ApiModelProperty("工作列表设置")
    private Long workListSet;

    @ApiModelProperty("就诊操作设置")
    private Long visitOperateSet;
}